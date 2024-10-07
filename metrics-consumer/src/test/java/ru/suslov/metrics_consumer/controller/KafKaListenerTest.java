package ru.suslov.metrics_consumer.controller;

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
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.service.MetricService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class KafKaListenerTest {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private KafkaProducer<String, MetricDto> testKafkaProducer;
    private final MetricService metricService;

    @Autowired
    public KafKaListenerTest(MetricService metricService) {
        this.metricService = metricService;
    }

//    todo before All

    @BeforeEach
    void beforeAll() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        testKafkaProducer = new KafkaProducer<>(props);

//        Awaitility.setDefaultTimeout(Duration.ofSeconds(15));
//        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
    }

    @Test
    void kafkaListener() {
        var value = new HashMap<String, Object>();
        value.put("metric1", "test metric value 5");
        value.put("metric2", 30);

        var metricDto = new MetricDto("my-app-" + UUID.randomUUID(), OffsetDateTime.now(), value);

        ProducerRecord<String, MetricDto> record = new ProducerRecord<>("metrics-topic", metricDto);

        try {
            testKafkaProducer.send(record).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

//        В тестах странно может воспроизводиться баг - не проходит assertThat(), может помочь перезапуск кафка - $ docker-compose down, $ docker-compose up
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    var metric = metricService.findByApplicationName(metricDto.getApplicationName());
                    assertThat(metric.get().getApplicationName()).isEqualTo(metricDto.getApplicationName());
//                    assertThat(metric).isPresent();
//                    assertThat(metric.get().getCode()).isEqualTo("P100");
                });

//        await().atMost(Duration.ofSeconds(6L)).untilAsserted(() -> assertThat(metricService.findByApplicationName(metricDto.getApplicationName()).get().getApplicationName()).isEqualTo(metricDto.getApplicationName()));
//        await().until(() -> Objects.equals(metricDto.getApplicationName(), metricService.findByApplicationName(metricDto.getApplicationName()).get().getApplicationName()));
    }
}