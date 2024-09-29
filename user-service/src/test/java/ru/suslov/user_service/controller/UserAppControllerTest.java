package ru.suslov.user_service.controller;

import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.dto.UserAppDto;
import ru.suslov.user_service.service.UserAppService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class UserAppControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final TestRestTemplate testRestTemplate;
    private final ModelMapper modelMapper;
    private final UserAppService userAppService;

    @Autowired
    public UserAppControllerTest(TestRestTemplate testRestTemplate, UserAppService userAppService, ModelMapper modelMapper) {
        this.testRestTemplate = testRestTemplate;
        this.userAppService = userAppService;
        this.modelMapper = modelMapper;
    }

//    @Test
//    void postUserApp() {
//        JSONObject personJson = new JSONObject();
//        personJson.put("username", "Ivanov2000");
//        personJson.put("firstName", "Petr");
//        personJson.put("secondName", "Ivanov");
//
//        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/users", personJson, UserAppDto.class);
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        userAppService.delete(userAppService.findById(response.getBody().getId()).orElse(null));
//    }

    @Test
    void getHealthWithoutAuth() {
        var response = testRestTemplate.getForEntity(RESOURCE_URL + localPort + "/v1/admin/health", UserAppDto.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private BearerToken getAuthHeaderForUser() {
        JSONObject personJson = new JSONObject();
        personJson.put("username", "Ivanov2000");
        personJson.put("firstName", "Petr");
        personJson.put("secondName", "Ivanov");
        personJson.put("email", "ivanov@yandex.ru");
        personJson.put("password", "password");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", personJson, BearerToken.class);

        return response.getBody();
    }

    @Test
    void getHealth() {
        BearerToken tokent = getAuthHeaderForUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokent.getAccessToken());
        ResponseEntity<String> response = testRestTemplate.exchange("/v1/admin/health", HttpMethod.GET, new HttpEntity<>(headers), String.class);

//        var response = testRestTemplate.getForEntity(RESOURCE_URL + localPort + "/v1/admin/health", UserAppDto.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
