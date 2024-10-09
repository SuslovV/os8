package ru.suslov.perfomance_statistics.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.perfomance_statistics.dto.PerfomanceStatisticsAggregateDto;
import ru.suslov.perfomance_statistics.model.PerfomanceStatistics;
import ru.suslov.perfomance_statistics.service.PerfomanceStatisticsService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class PerfomanceStatisticsControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final TestRestTemplate testRestTemplate;
    private final PerfomanceStatisticsService perfomanceStatisticsService;
    private PerfomanceStatistics perfomanceStatistics;

    @Autowired
    public PerfomanceStatisticsControllerTest(TestRestTemplate testRestTemplate, PerfomanceStatisticsService perfomanceStatisticsService) {
        this.testRestTemplate = testRestTemplate;
        this.perfomanceStatisticsService = perfomanceStatisticsService;
    }

    @BeforeEach
    public void init() throws ExecutionException, InterruptedException {
        perfomanceStatistics = perfomanceStatisticsService.save("MethodController", "method_get", 10).get();
    }

    @AfterEach
    public void after() {
        perfomanceStatisticsService.delete(perfomanceStatistics);
    }

    @Test
    void getPerfomanceStatisticsPage() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/perfomance-statistics?page=0&size=100", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PerfomanceStatistics>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).size().isGreaterThan(0);
    }

    @Test
    void perfomanceStatisticsAggregate() {
        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/perfomance-statistics/aggregate", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PerfomanceStatisticsAggregateDto>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).size().isGreaterThan(0);
    }

}
