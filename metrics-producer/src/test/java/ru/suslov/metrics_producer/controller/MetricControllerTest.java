package ru.suslov.metrics_producer.controller;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.suslov.metrics_producer.dto.MetricDto;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MetricControllerTest {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;
    private final TestRestTemplate testRestTemplate;
    private KafkaConsumer<String, MetricDto> consumer;

    @Autowired
    public MetricControllerTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    @BeforeEach
    public void init() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "metric-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, (new JsonDeserializer<>(MetricDto.class)).getClass()); to do
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "MetricDto:ru.suslov.metrics_producer.dto.MetricDto");

        consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("metrics-topic"));

    }

    @AfterEach
    public void after() {
    }

    @Test
    void postMetrics() {
        var value = new HashMap<String, Object>();
        value.put("metric1", "test metric value 5");
        value.put("metric2", 20);
        var metricDto = new MetricDto("my-app", OffsetDateTime.now(), value);
        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/metrics", metricDto, Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        await().pollInterval(Duration.ofSeconds(3)).atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            ConsumerRecords<String, MetricDto> records = consumer.poll(Duration.ofMillis(4000));

            Assertions.assertThat(records.count()).isGreaterThan(0);
        });

    }
}
