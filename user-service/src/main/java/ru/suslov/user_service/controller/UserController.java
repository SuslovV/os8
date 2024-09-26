package ru.suslov.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.suslov.user_service.dto.UserDto;
import ru.suslov.user_service.model.User;
import ru.suslov.user_service.service.UserService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<UserDto>>() {
    }.getType();

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get a list of users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/users")
    public List<UserDto> users(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(userService.findAll(pageable).getContent(), listType);
    }

    @Operation(summary = "Add user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json")
            })
//            ,
//            @ApiResponse(responseCode = "500", content = {
//                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
//            })
    })
    @PostMapping("/users")
    public UserDto users(@RequestBody UserDto userDto, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(userService.add(userDto.getUsername()), UserDto.class);
    }
}
