package ru.suslov.http_logging_spring_boot_starter.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ResourceDto {

    private UUID id;
    private String name;
    private UUID serverId;
    private String serverName;
    private Boolean active;
    private Boolean deleted;

}
