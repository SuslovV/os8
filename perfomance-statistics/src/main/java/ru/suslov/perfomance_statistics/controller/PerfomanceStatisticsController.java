package ru.suslov.perfomance_statistics.controller;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import ru.suslov.perfomance_statistics.annotation.TrackAsyncTime;
import ru.suslov.perfomance_statistics.annotation.TrackTime;
import ru.suslov.perfomance_statistics.dto.ClassSourceCodeDto;
import ru.suslov.perfomance_statistics.dto.MethodDto;
import ru.suslov.perfomance_statistics.dto.PerfomanceStatisticsAggregateDto;
import ru.suslov.perfomance_statistics.dto.PerfomanceStatisticsDto;
import ru.suslov.perfomance_statistics.service.PerfomanceStatisticsService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@Slf4j
public class PerfomanceStatisticsController {
    private final PerfomanceStatisticsService perfomanceStatisticsService;
    private final ModelMapper modelMapper;
    private final Type listType = new TypeToken<List<PerfomanceStatisticsDto>>() {
    }.getType();

    @Autowired
    public PerfomanceStatisticsController(PerfomanceStatisticsService perfomanceStatisticsService, ModelMapper modelMapper) {
        this.perfomanceStatisticsService = perfomanceStatisticsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get a list of perfomance statistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = PerfomanceStatisticsDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/perfomance-statistics")
    @TrackAsyncTime
    @Async
    public CompletableFuture<List<PerfomanceStatisticsDto>> perfomanceStatistics(@PageableDefault(size = 100, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        return CompletableFuture.supplyAsync(() -> modelMapper.map(perfomanceStatisticsService.findAll(pageable).getContent(), listType));
    }

    @Operation(summary = "Get a list of aggregate perfomance statistics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = PerfomanceStatisticsAggregateDto.class)),
                            mediaType = "application/json")
            })
    })
    @GetMapping("/perfomance-statistics/aggregate")
    @TrackTime
    public List<PerfomanceStatisticsAggregateDto> perfomanceStatisticsAggregateByMethod(@PageableDefault(size = 20, sort = "id") Pageable pageable, HttpServletRequest request, Map<String, String> headers) {
        var classStatistics = perfomanceStatisticsService.getPerfomanceStatisticsGroupByMethod();
        return classStatistics.stream().map((el) -> {
            var methodPerfomanceStatisticsDto = new PerfomanceStatisticsAggregateDto();

            MethodDto methodDto = new MethodDto();
            methodDto.setId((UUID) el[0]);
            methodDto.setName((String) el[2]);
            ClassSourceCodeDto classSourceCodeDto = new ClassSourceCodeDto();
            classSourceCodeDto.setName((String) el[1]);
            methodDto.setClassSourceCode(classSourceCodeDto);

            methodPerfomanceStatisticsDto.setMethod(methodDto);
            methodPerfomanceStatisticsDto.setExecutionTimeSum(((Long) el[3]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeAvg(((Long) el[4]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeMin(((Long) el[5]).longValue());
            methodPerfomanceStatisticsDto.setExecutionTimeMax(((Long) el[6]).longValue());
            methodPerfomanceStatisticsDto.setCountCall((Long) el[7]);
            return methodPerfomanceStatisticsDto;
        }).collect(Collectors.toList());
    }

}
