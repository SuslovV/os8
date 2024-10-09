package ru.suslov.perfomance_statistics.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ClassSourceCodeDto {

    private UUID id;
    private String name;
    private Boolean active;
    private Boolean deleted;
    private OffsetDateTime createdTime;
    private OffsetDateTime lastModifiedTime;

}
