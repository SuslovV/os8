package ru.suslov.http_logging_spring_boot_starter.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.http_logging_spring_boot_starter.dto.HttpRequestLogDto;
import ru.suslov.http_logging_spring_boot_starter.model.HttpRequestLog;
import ru.suslov.http_logging_spring_boot_starter.model.Resource;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.service.HttpRequestLogService;
import ru.suslov.http_logging_spring_boot_starter.service.ResourceService;
import ru.suslov.http_logging_spring_boot_starter.service.ServerService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class HttpRequestLogControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final HttpRequestLogService httpRequestLogService;
    private final ResourceService resourceService;
    private final ServerService serverService;
    private final TestRestTemplate testRestTemplate;
    private Server server;
    private Resource resource;
    private HttpRequestLog httpRequestLog;

    @Autowired
    public HttpRequestLogControllerTest(HttpRequestLogService httpRequestLogService, TestRestTemplate testRestTemplate, ResourceService resourceService, ServerService serverService) {
        this.httpRequestLogService = httpRequestLogService;
        this.testRestTemplate = testRestTemplate;
        this.resourceService = resourceService;
        this.serverService = serverService;
    }

    @BeforeEach
    public void init() {
        server = serverService.findByName("localhost").orElseGet(() -> {
            server = new Server();
            server.setName("localhost");
            return serverService.save(server);
        });

        resource = resourceService.findByNameAndServer("/v1/health", server).orElseGet(() -> {
            resource = new Resource();
            resource.setName("/v1/health");
            resource.setServer(server);
            return resourceService.save(resource);
        });

        httpRequestLog = new HttpRequestLog();
        httpRequestLog.setResource(resource);
        httpRequestLogService.save(httpRequestLog);
    }

    @AfterEach
    public void after() {
        httpRequestLogService.delete(httpRequestLog);
    }

    @Test
    void getHttpRequestLogPage() {
        ResponseEntity<ArrayList<HttpRequestLogDto>> response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/http-request-log?page=0&size=100", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).size().isGreaterThan(0);
    }

}
