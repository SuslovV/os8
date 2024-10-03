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
import ru.suslov.user_service.model.Role;
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
    private BearerToken token;

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
        token = response.getBody();

    }

    @AfterEach
    public void after() {
        userAppService.delete(userApp);
    }

    @Test
    void getHello_WithoutAuth() {
        var response = testRestTemplate.getForEntity(RESOURCE_URL + localPort + "/v1/hello/user", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getHello() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        ResponseEntity<String> response = testRestTemplate.exchange("/v1/hello/user", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHello_BadRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        ResponseEntity<String> response = testRestTemplate.exchange("/v1/hello/admin", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getHelloWithAuthenticate_User() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov2000");
        userJson.put("password", "password");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", userJson, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        var response2 = testRestTemplate.exchange("/v1/hello/user", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWithAuthenticate_Admin() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov2000");
        userJson.put("password", "password");

        userApp.getRoles().add(Role.ADMIN);
        userAppService.save(userApp);

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", userJson, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());

        var response2 = testRestTemplate.exchange("/v1/hello/admin", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWith_RefreshToken() {
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("value", token.getRefreshToken());

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/refreshtoken", tokenJson, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        var response2 = testRestTemplate.exchange("/v1/hello/user", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWithAuthenticate_BadPassword() {
        JSONObject loginDtoJson = new JSONObject();
        loginDtoJson.put("username", "Ivanov2000");
        loginDtoJson.put("password", "bad password");

        ResponseEntity<byte[]> response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", loginDtoJson, byte[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // todo token expirationTime

    @Test
    void postUser_ConflictEmail() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov3");
        userJson.put("firstName", "Petr");
        userJson.put("secondName", "Ivanov");
        userJson.put("email", "ivanov@yandex.ru");
        userJson.put("password", "password");

        ResponseEntity<byte[]> response =
                testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", userJson, byte[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void postUser_ConflictUsername() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", "Ivanov2000");
        userJson.put("firstName", "Petr");
        userJson.put("secondName", "Petrov");
        userJson.put("email", "petrov@yandex.ru");
        userJson.put("password", "password");

        ResponseEntity<byte[]> response =
                testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", userJson, byte[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

}
