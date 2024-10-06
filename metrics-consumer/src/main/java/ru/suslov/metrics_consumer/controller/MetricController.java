package ru.suslov.metrics_consumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.suslov.metrics_consumer.dto.MetricDto;
import ru.suslov.metrics_consumer.service.MetricService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class MetricController {
    private final MetricService metricService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<MetricDto>>() {
    }.getType();

    @Autowired
    public MetricController(MetricService metricService, ModelMapper modelMapper) {
        this.metricService = metricService;
        this.modelMapper = modelMapper;
    }


    @Operation(summary = "Get metric by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = MetricDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/metrics/{id}")
    public MetricDto metrics(@PathVariable("id") String id, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(metricService.findById(UUID.fromString(id)), MetricDto.class);
    }

    @Operation(summary = "Get a list of metrics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = MetricDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/metrics")
    public List<MetricDto> metrics(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return modelMapper.map(metricService.findAll(pageable).getContent(), listType);
    }

}
