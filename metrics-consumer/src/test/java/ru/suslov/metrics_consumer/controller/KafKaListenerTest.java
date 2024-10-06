package ru.suslov.metrics_consumer.controller;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.service.MetricService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class KafKaListenerTest {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private KafkaProducer<String, MetricDto> testKafkaProducer;
    private final MetricService metricService;
    private final TestRestTemplate testRestTemplate;


    @Autowired
    public KafKaListenerTest(MetricService metricService, TestRestTemplate testRestTemplate) {
        this.metricService = metricService;
        this.testRestTemplate = testRestTemplate;
    }

//    todo before All

    @BeforeEach
    void beforeAll() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/metrics?page=0&size=100", HttpMethod.GET, null, new ParameterizedTypeReference<List<MetricDto>>() {
        });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        testKafkaProducer = new KafkaProducer<>(props);

        Awaitility.setDefaultTimeout(Duration.ofSeconds(15));
        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
    }

    @Test
    void kafkaListener() {
        var value = new HashMap<String, Object>();
        value.put("metric1", "test metric value 5");
        value.put("metric2", 30);

        var metricDto = new MetricDto("my-app-" + UUID.randomUUID(), OffsetDateTime.now(), value);

        ProducerRecord<String, MetricDto> record = new ProducerRecord<>("metrics-topic", metricDto);

//        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
//        assertTrue(messageConsumed);
//        assertThat(consumer.getPayload(), containsString(data));

        try {
            testKafkaProducer.send(record).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        await().atMost(Duration.ofSeconds(6L)).untilAsserted(() -> assertThat(metricService.findByApplicationName(metricDto.getApplicationName()).get().getApplicationName()).isEqualTo(metricDto.getApplicationName()));
//        await().until(() -> Objects.equals(metricDto.getApplicationName(), metricService.findByApplicationName(metricDto.getApplicationName()).get().getApplicationName()));
    }
}