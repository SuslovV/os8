package ru.suslov.user_service.controller;

import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
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
import ru.suslov.user_service.dto.UserAppDto;
import ru.suslov.user_service.model.UserApp;
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
//    void getUserAppPage() {
//        var response = testRestTemplate.exchange(RESOURCE_URL + localPort + "/v1/users?page=0&size=100", HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<UserAppDto>>() {
//                });
////        userStatisticsService.save(PerfomanceStatisticsController.class.getName(), , 10);
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }

    @Test
    void postUserApp() {
        JSONObject personJson = new JSONObject();
        personJson.put("username", "Ivanov2000");
        personJson.put("firstName", "Petr");
        personJson.put("secondName", "Ivanov");

        var response = testRestTemplate.postForEntity(RESOURCE_URL + localPort + "/v1/users", personJson, UserAppDto.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        userAppService.delete(userAppService.findById(response.getBody().getId()).orElse(null));
    }

}
