package ru.suslov.metrics_producer.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import ru.suslov.metrics_producer.dto.MetricDto;

import java.time.OffsetDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MetricControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;
    private final TestRestTemplate testRestTemplate;

    @Autowired
    public MetricControllerTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    @BeforeEach
    public void init() {
    }

    @AfterEach
    public void after() {
    }

    @Test
    void metrics() {
        var value = new HashMap<String, Object>();
        value.put("metric1", "test metric value 5");
        value.put("metric2", 20);
        var metricDto = new MetricDto("my-app", OffsetDateTime.now(), value);
        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/metrics", metricDto, Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}