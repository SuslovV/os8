package ru.suslov.http_logging_spring_boot_starter.controller;

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
import ru.suslov.http_logging_spring_boot_starter.dto.ResourceDto;
import ru.suslov.http_logging_spring_boot_starter.service.ResourceService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class ResourceController {
    private final Logger logger = LoggerFactory.getLogger(ResourceController.class.getName());
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<ResourceDto>>() {
    }.getType();

    @Autowired
    public ResourceController(ResourceService resourceService, ModelMapper modelMapper) {
        this.resourceService = resourceService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get a list of resources")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ResourceDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/resources")
    public List<ResourceDto> resources(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(resourceService.findAll(pageable).getContent(), listType);
    }
}
