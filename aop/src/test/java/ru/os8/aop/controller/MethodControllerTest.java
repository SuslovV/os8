package ru.os8.aop.controller;

import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.os8.aop.model.Method;
import ru.os8.aop.service.MethodService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class MethodControllerTest {
    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;
    private final TestRestTemplate testRestTemplate;
    private final MethodService methodService;

    @Autowired
    public MethodControllerTest(TestRestTemplate testRestTemplate, MethodService methodService) {
        this.testRestTemplate = testRestTemplate;
        this.methodService = methodService;
    }

    @Test
    void postMethods() {
        JSONObject personJson = new JSONObject();
        personJson.put("name", "method_get");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/methods", personJson, Method.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        methodService.delete(response.getBody());
    }
}
