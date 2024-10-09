package ru.suslov.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.dto.LoginDto;
import ru.suslov.user_service.dto.RefreshTokenDto;
import ru.suslov.user_service.dto.RegisterUserDto;
import ru.suslov.user_service.service.UserAppService;

@RestController
@RequestMapping("/v1")
@Slf4j
public class AuthController {
    private final UserAppService userAppService;

    @Autowired
    public AuthController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Operation(summary = "User registration")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = BearerToken.class)),
                            mediaType = "application/json")
            })
    })
    @PostMapping("/auth/register")
    public BearerToken register(@RequestBody RegisterUserDto registerUserDto) {
        return userAppService.register(registerUserDto);
    }

    @Operation(summary = "User authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = BearerToken.class)),
                            mediaType = "application/json")
            })
    })
    @PostMapping("/auth/authenticate")
    public BearerToken authenticate(@RequestBody LoginDto loginDto) {
        return userAppService.authenticate(loginDto);
    }

    @Operation(summary = "Refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = BearerToken.class)),
                            mediaType = "application/json")
            })
    })
    @PostMapping("/auth/refresh-token")
    public BearerToken refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return userAppService.refreshToken(refreshTokenDto);
    }

}
