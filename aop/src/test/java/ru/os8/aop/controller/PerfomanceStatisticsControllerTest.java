package ru.os8.aop.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.os8.aop.dto.PerfomanceStatisticsAggregateDto;
import ru.os8.aop.model.PerfomanceStatistics;
import ru.os8.aop.service.PerfomanceStatisticsService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class PerfomanceStatisticsControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final TestRestTemplate testRestTemplate;
    private final PerfomanceStatisticsService perfomanceStatisticsService;

    @Autowired
    public PerfomanceStatisticsControllerTest(TestRestTemplate testRestTemplate, PerfomanceStatisticsService perfomanceStatisticsService) {
        this.testRestTemplate = testRestTemplate;
        this.perfomanceStatisticsService = perfomanceStatisticsService;
    }

    @Test
    void getPerfomanceStatisticsPage() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/perfomancestatistics?page=0&size=100", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PerfomanceStatistics>>() {
                });
//        perfomanceStatisticsService.save(PerfomanceStatisticsController.class.getName(), , 10);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void perfomanceStatisticsAggregate() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/perfomancestatistics/aggregate", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PerfomanceStatisticsAggregateDto>>() {
                });
//        perfomanceStatisticsService.save(PerfomanceStatisticsController.class.getName(), , 10);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(response.getBody()).size().isNotNull();
    }

}
