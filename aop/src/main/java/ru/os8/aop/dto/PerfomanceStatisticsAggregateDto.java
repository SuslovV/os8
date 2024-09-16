package ru.os8.aop.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PerfomanceStatisticsAggregateDto {

    private UUID methodId;

    private String className;
    private String methodName;

    private long executionTimeSum;
    private long executionTimeAvg;
    private long executionTimeMin;
    private long executionTimeMax;
    private long countCall;

}
