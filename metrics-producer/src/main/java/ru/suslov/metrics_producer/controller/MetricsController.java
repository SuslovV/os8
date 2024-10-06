package ru.suslov.metrics_producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.suslov.metrics_producer.dto.MetricDto;
import ru.suslov.metrics_producer.service.KafkaProducerService;

@RestController
@RequestMapping("/v1")
@Slf4j
public class MetricsController {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public MetricsController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(summary = "Add metric")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @PostMapping(value ="/metrics")
    public void metrics(@RequestBody MetricDto metricDto) throws JsonProcessingException {
        kafkaProducerService.send("metrics-topic", metricDto);

    }

}
