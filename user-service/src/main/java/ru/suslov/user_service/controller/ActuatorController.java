package ru.suslov.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ActuatorController {
    private static final String API_VERSION = "v1";

    @GetMapping(value = API_VERSION + "/health")
    public String health() {
        return "ok";
    }

    @GetMapping(API_VERSION + "/hello/user")
    public ResponseEntity<String> helloUser() {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello user " + user.getPrincipal() + "!");
    }

    @GetMapping(API_VERSION + "/hello/admin")
    public ResponseEntity<String> helloAdmin() {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello admin " + user.getPrincipal() +  "!");
    }
}
