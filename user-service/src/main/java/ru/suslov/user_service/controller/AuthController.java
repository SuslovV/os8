package ru.suslov.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.dto.LoginDto;
import ru.suslov.user_service.dto.RegisterUserDto;
import ru.suslov.user_service.dto.UserAppDto;
import ru.suslov.user_service.service.UserAppService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@Slf4j
public class AuthController {
    private final UserAppService userAppService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<UserAppDto>>() {
    }.getType();

    @Autowired
    public AuthController(UserAppService userAppService, ModelMapper modelMapper) {
        this.userAppService = userAppService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/auth/register")
    public BearerToken register(@RequestBody RegisterUserDto registerUserDto) throws Exception {
        return userAppService.register(registerUserDto);
    }


    @PostMapping("/auth/authenticate")
    public String authenticate(@RequestBody LoginDto loginDto) {
        return userAppService.authenticate(loginDto);
    }

}
