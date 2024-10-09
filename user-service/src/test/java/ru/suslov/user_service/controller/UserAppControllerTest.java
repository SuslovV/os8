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
import ru.suslov.user_service.dto.LoginDto;
import ru.suslov.user_service.dto.RegisterUserDto;
import ru.suslov.user_service.model.Role;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.service.RefreshTokenService;
import ru.suslov.user_service.service.UserAppService;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class UserAppControllerTest {

    private static final String RESOURCE_URL = "http://localhost:";

    @LocalServerPort
    private int localPort;

    private final ModelMapper modelMapper;
    private final RefreshTokenService refreshTokenService;
    private final TestRestTemplate testRestTemplate;
    private final UserAppService userAppService;
    private UserApp userApp;
    private LoginDto loginDto;
    private RegisterUserDto registerUserDto;
    private BearerToken token;

    @Autowired
    public UserAppControllerTest(TestRestTemplate testRestTemplate, UserAppService userAppService, ModelMapper modelMapper, RefreshTokenService refreshTokenService) {
        this.testRestTemplate = testRestTemplate;
        this.userAppService = userAppService;
        this.modelMapper = modelMapper;
        this.refreshTokenService = refreshTokenService;

        registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("Ivanov2000");
        registerUserDto.setFirstName("Petr");
        registerUserDto.setSecondName("Ivanov");
        registerUserDto.setEmail("ivanov@yandex.ru");
        registerUserDto.setPassword("password");

        loginDto = new LoginDto();
        loginDto.setUsername("Ivanov2000");
        loginDto.setPassword("password");
    }

    @BeforeEach
    public void init() throws IOException {
        userApp = userAppService.findByEmail("ivanov@yandex.ru").orElse(null);
        if (userApp != null) {
            userAppService.delete(userApp);
        }

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", registerUserDto, BearerToken.class);
        userApp = userAppService.findByEmail("ivanov@yandex.ru").orElse(null);
        token = response.getBody();
    }

    @AfterEach
    public void after() {
        userAppService.delete(userApp);
        refreshTokenService.deleteAllByUserId(UUID.fromString(token.getRefreshToken()));
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
        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", loginDto, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        var response2 = testRestTemplate.exchange("/v1/hello/user", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWithAuthenticate_Admin() {
        userApp.getRoles().add(Role.ADMIN);
        userAppService.save(userApp);

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", loginDto, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());

        var response2 = testRestTemplate.exchange("/v1/hello/admin", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        userApp.getRoles().remove(Role.ADMIN);
        userAppService.save(userApp);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWith_RefreshToken() {
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("value", token.getRefreshToken());

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/refresh-token", tokenJson, BearerToken.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getAccessToken());
        var response2 = testRestTemplate.exchange("/v1/hello/user", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getHelloWithAuthenticate_BadPassword() {
        var passwordPrev = registerUserDto.getPassword();
        loginDto.setPassword("bad password");

        ResponseEntity<byte[]> response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/authenticate", loginDto, byte[].class);
        loginDto.setPassword(passwordPrev);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    // todo token expirationTime

    @Test
    void postUser_ConflictEmail() {
        var usernamePrev = registerUserDto.getUsername();
        registerUserDto.setUsername("Ivanov3");

        ResponseEntity<byte[]> response =
                testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", registerUserDto, byte[].class);
        registerUserDto.setUsername(usernamePrev);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void postUser_ConflictUsername() {
        var emailPrev = registerUserDto.getEmail();
        registerUserDto.setEmail("petrov@yandex.ru");

        ResponseEntity<byte[]> response =
                testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/auth/register", registerUserDto, byte[].class);
        registerUserDto.setEmail(emailPrev);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

}
