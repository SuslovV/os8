package ru.suslov.perfomance_statistics.dto;

import lombok.Data;

@Data
public class PerfomanceStatisticsAggregateDto {

    private MethodDto method;
    private long executionTimeSum;
    private long executionTimeAvg;
    private long executionTimeMin;
    private long executionTimeMax;
    private long countCall;

}
