package ru.suslov.http_logging_spring_boot_starter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
public class ActuatorController2 {
    private static final String API_VERSION = "v1";

    RestTemplate restTemplate;

    @Autowired
    public ActuatorController2(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = API_VERSION + "/health")
    public ResponseEntity<String> health() {
        restTemplate.getForEntity("http://localhost:8080/v1/health2", null);

        return new ResponseEntity("yes 22", HttpStatus.CREATED);
    }

    @GetMapping(value = API_VERSION + "/health2")
    public ResponseEntity<String> health2() {
        return new ResponseEntity("yes 33", HttpStatus.ACCEPTED);
    }
}
