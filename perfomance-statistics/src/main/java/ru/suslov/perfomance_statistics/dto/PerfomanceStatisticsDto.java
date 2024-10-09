package ru.suslov.perfomance_statistics.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class PerfomanceStatisticsDto {

    private UUID id;
    private MethodDto method;
    private long executionTime;
    private OffsetDateTime lastModifiedTime;

}
