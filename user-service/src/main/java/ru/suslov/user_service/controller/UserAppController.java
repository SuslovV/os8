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
import org.springframework.web.bind.annotation.*;
import ru.suslov.user_service.dto.UserAppDto;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.service.UserAppService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@Slf4j
public class UserAppController {
    private final UserAppService userAppService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<UserAppDto>>() {
    }.getType();

    @Autowired
    public UserAppController(UserAppService userAppService, ModelMapper modelMapper) {
        this.userAppService = userAppService;
        this.modelMapper = modelMapper;
    }

//    @Operation(summary = "Add user")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", content = {
//                    @Content(schema = @Schema(implementation = UserApp.class), mediaType = "application/json")
//            })
////            ,
////            @ApiResponse(responseCode = "500", content = {
////                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
////            })
//    })
//    @PostMapping("/users")
//    public UserAppDto users(@RequestBody UserAppDto userAppDto, HttpServletRequest request, Map<String, String> headers) {
//        UserApp userApp = modelMapper.map(userAppDto, UserApp.class);
//
//        return modelMapper.map(userAppService.add(userApp), UserAppDto.class);
//    }

}
