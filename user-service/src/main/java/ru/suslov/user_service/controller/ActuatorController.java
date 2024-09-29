package ru.suslov.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suslov.user_service.dto.UserAppDto;

@RestController
@RequestMapping("/")
public class ActuatorController {
    private static final String API_VERSION = "v1";

    @GetMapping(value = API_VERSION + "/admin/health")
    public UserAppDto health() {
        return new UserAppDto();
    }
}
