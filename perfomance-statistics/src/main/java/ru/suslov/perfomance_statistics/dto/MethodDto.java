package ru.suslov.perfomance_statistics.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MethodDto {

    private UUID id;
    private String name;
    private UUID classSourceCodeId;
    private String classSourceCodeName;
    private Boolean active;
    private Boolean deleted;

}
