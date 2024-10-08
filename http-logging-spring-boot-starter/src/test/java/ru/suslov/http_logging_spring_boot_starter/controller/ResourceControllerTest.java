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
import ru.suslov.http_logging_spring_boot_starter.dto.ResourceDto;
import ru.suslov.http_logging_spring_boot_starter.model.Resource;
import ru.suslov.http_logging_spring_boot_starter.model.Server;
import ru.suslov.http_logging_spring_boot_starter.service.ResourceService;
import ru.suslov.http_logging_spring_boot_starter.service.ServerService;

import java.io.IOException;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class ResourceControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final ResourceService resourceService;
    private final ServerService serverService;
    private final TestRestTemplate testRestTemplate;
    private Server server;
    private Resource resource;

    @Autowired
    public ResourceControllerTest(TestRestTemplate testRestTemplate, ResourceService resourceService, ServerService serverService) {
        this.testRestTemplate = testRestTemplate;
        this.resourceService = resourceService;
        this.serverService = serverService;
    }

    @BeforeEach
    public void init() throws IOException {
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
    }

    @AfterEach
    public void after() {
//        resourceService.delete(resource);
//        serverService.delete(server);
    }

    @Test
    void getResourcePage() {
        ResponseEntity<List<ResourceDto>> response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/resources?page=0&size=100", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).size().isNotNull();
    }

}
