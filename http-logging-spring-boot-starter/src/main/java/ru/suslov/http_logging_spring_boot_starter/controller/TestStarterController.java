package ru.suslov.http_logging_spring_boot_starter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1")
public class TestStarterController {

    private final RestTemplate restTemplate;

    @Autowired
    public TestStarterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/health")
    public ResponseEntity<String> health() {
        restTemplate.getForEntity("http://localhost:8080/v1/health2", null);

        return new ResponseEntity("yes 22", HttpStatus.OK);
    }

    @GetMapping(value = "/health2")
    public ResponseEntity<String> health2() {
        return new ResponseEntity("yes 33", HttpStatus.OK);
    }
}
