package ru.os8.aop.controller;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import ru.os8.aop.annotation.TrackAsyncTime;
import ru.os8.aop.annotation.TrackTime;
import ru.os8.aop.dto.PerfomanceStatisticsAggregateDto;
import ru.os8.aop.dto.PerfomanceStatisticsDto;
import ru.os8.aop.service.PerfomanceStatisticsService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@Slf4j
public class PerfomanceStatisticsController {
    private final PerfomanceStatisticsService perfomanceStatisticsService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<PerfomanceStatisticsDto>>(){}.getType();

    @Autowired
    public PerfomanceStatisticsController(PerfomanceStatisticsService perfomanceStatisticsService, ModelMapper modelMapper) {
        this.perfomanceStatisticsService = perfomanceStatisticsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "get list PerfomanceStatistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PerfomanceStatisticsDto.class), mediaType = "application/json")
            })
    })
    @GetMapping("/perfomancestatistics")
    public List<PerfomanceStatisticsDto> perfomanceStatistics(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) throws ExecutionException, InterruptedException {
        perfomanceStatisticsService.testAsync();
        return modelMapper.map(perfomanceStatisticsService.findAll(pageable).getContent(), listType);
    }

    @Operation(summary = "get list aggregate PerfomanceStatistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PerfomanceStatisticsAggregateDto.class), mediaType = "application/json")
            })
    })
    @GetMapping("/perfomancestatistics/aggregate")
    @TrackTime
    public List<PerfomanceStatisticsAggregateDto> perfomanceStatisticsAggregateByMethod(@PageableDefault(size = 20, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        var classStatistics = perfomanceStatisticsService.getPerfomanceStatisticsGroupByMethod();
        return classStatistics.stream().map((el) -> {
            var methodPerfomanceStatisticsDto = new PerfomanceStatisticsAggregateDto();
            methodPerfomanceStatisticsDto.setMethodId((UUID) el[0]);
            methodPerfomanceStatisticsDto.setClassName((String) el[1]);
            methodPerfomanceStatisticsDto.setMethodName((String) el[2]);
            methodPerfomanceStatisticsDto.setExecutionTimeSum(((Long) el[3]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeAvg(((Long) el[4]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeMin(((Long) el[5]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeMax(((Long) el[6]).longValue());
            methodPerfomanceStatisticsDto.setCountCall((Long) el[7]);
            return methodPerfomanceStatisticsDto;
        }).collect(Collectors.toList());
    }

}
