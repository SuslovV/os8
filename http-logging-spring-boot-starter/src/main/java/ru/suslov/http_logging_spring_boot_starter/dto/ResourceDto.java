package ru.suslov.http_logging_spring_boot_starter.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ResourceDto {

    private UUID id;
    private String name;
    private ServerDto server;
    private Boolean active;
    private Boolean deleted;
    private OffsetDateTime createdTime;
    private OffsetDateTime lastModifiedTime;

}
