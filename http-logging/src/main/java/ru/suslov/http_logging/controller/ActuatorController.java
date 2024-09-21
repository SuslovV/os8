package ru.suslov.http_logging.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ActuatorController {
    private static final String API_VERSION = "v1";

    @GetMapping(value = API_VERSION + "/health")
    public void health() {
    }
}
