package ru.suslov.http_logging_spring_boot_starter.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class HttpRequestLogDto {

    private UUID id;

    private String resource;

    private Long executionTime;

    private boolean response;

    private String method;

    private Integer httpStatus;

   private String requestType;

    private Map<String, String> headers;

    private UUID httpRequestId;

    private OffsetDateTime createdTime;

    private OffsetDateTime lastModifiedTime;

}
