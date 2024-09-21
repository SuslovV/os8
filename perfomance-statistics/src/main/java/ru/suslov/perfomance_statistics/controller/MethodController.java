package ru.suslov.perfomance_statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.suslov.perfomance_statistics.annotation.TrackTime;
import ru.suslov.perfomance_statistics.dto.MethodDto;
import ru.suslov.perfomance_statistics.model.Method;
import ru.suslov.perfomance_statistics.service.MethodService;

import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping("/v1")
public class MethodController {
    private final Logger logger = LoggerFactory.getLogger(MethodController.class.getName());
    private final MethodService methodService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<MethodDto>>() {
    }.getType();

    @Autowired
    public MethodController(MethodService methodService, ModelMapper modelMapper) {
        this.methodService = methodService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get a list of methods")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = MethodDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/methods")
    public List<MethodDto> methods(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(methodService.findAll(pageable).getContent(), listType);
    }

    @Operation(summary = "Add method")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Method.class), mediaType = "application/json")
            })
//            ,
//            @ApiResponse(responseCode = "500", content = {
//                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
//            })
    })
    @PostMapping("/methods")
    @TrackTime
    public Method methods(@RequestBody MethodDto methodDto, HttpServletRequest request, Map<String, String> headers) {
        return methodService.save(modelMapper.map(methodDto, Method.class));
    }
}
