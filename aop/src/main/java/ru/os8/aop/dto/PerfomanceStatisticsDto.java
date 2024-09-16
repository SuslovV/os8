package ru.os8.aop.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class PerfomanceStatisticsDto {

    private UUID id;

    private String methodId;
    private String methodName;

    private long executionTime;
    private OffsetDateTime lastModifiedTime;

}
