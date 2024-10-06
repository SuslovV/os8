package ru.suslov.metrics_consumer.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.model.Metric;
import ru.suslov.metrics_consumer.service.MetricService;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class MetricControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final MetricService metricService;
    private final TestRestTemplate testRestTemplate;
    private Metric metric;

    @Autowired
    public MetricControllerTest(MetricService metricService, TestRestTemplate testRestTemplate) {
        this.metricService = metricService;
        this.testRestTemplate = testRestTemplate;
    }

    @BeforeEach
    public void init() {
        var value = new HashMap<String, Object>();
        value.put("metric1", "test metric value 7");
        value.put("metric2", 20);
        var metricDto = new MetricDto("my-app", OffsetDateTime.now(), value);
        metric = metricService.save(metricDto);
    }

    @AfterEach
    public void after() {
        metricService.delete(metric);
    }

    @Test
    void getMetric() {
        var response = testRestTemplate.getForEntity(RESOURCE_URL + localPort + "/v1/metrics/" + metric.getId(), MetricDto.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getValue().size()).isGreaterThan(0);

    }

    @Test
    void getMetric_List() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/metrics?page=0&size=100", HttpMethod.GET, null, new ParameterizedTypeReference<List<MetricDto>>() {
        });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().size()).isGreaterThan(0);

    }

}