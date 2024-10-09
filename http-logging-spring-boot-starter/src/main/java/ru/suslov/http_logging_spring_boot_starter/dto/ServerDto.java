package ru.suslov.http_logging_spring_boot_starter.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ServerDto {

    private UUID id;
    private String name;
    private String path;
    private Boolean active;
    private Boolean deleted;
    private OffsetDateTime createdTime;
    private OffsetDateTime lastModifiedTime;

}
