package ru.suslov.user_service.controller;

import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.service.UserAppService;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class UserAppControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final TestRestTemplate testRestTemplate;
    private final ModelMapper modelMapper;
    private final UserAppService userAppService;
    private UserApp userApp;
    private BearerToken tokent;

    @Autowired
    public UserAppControllerTest(TestRestTemplate testRestTemplate, UserAppService userAppService, ModelMapper modelMapper) {
        this.testRestTemplate = testRestTemplate;
        this.userAppService = userAppService;
        this.modelMapper = modelMapper;
    }

    @BeforeEach
    public void init() throws IOException {
        userApp = userAppService.findByEmail("ivanov@yandex.ru").orElse(null);
        if (userApp != null) {
            userAppService.delete(userApp);
        }

        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov2000");
        userJson.put("firstName", "Petr");
        userJson.put("secondName", "Ivanov");
        userJson.put("email", "ivanov@yandex.ru");
        userJson.put("password", "password");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", userJson, BearerToken.class);
        userApp = userAppService.findByEmail("ivanov@yandex.ru").orElse(null);
        tokent = response.getBody();

    }

    @AfterEach
    public void after() {
        userAppService.delete(userApp);
    }

    @Test
    void getHealth_WithoutAuth() {
        var response = testRestTemplate.getForEntity(RESOURCE_URL + localPort + "/v1/admin/health", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getHealth() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokent.getAccessToken());
        ResponseEntity<String> response = testRestTemplate.exchange("/v1/admin/health", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHealthWithAuthenticate() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov2000");
        userJson.put("password", "password");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", userJson, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tokent = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokent.getAccessToken());
        var response2 = testRestTemplate.exchange("/v1/admin/health", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
