package ru.suslov.user_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserAppDto {

    private UUID id;
    private String username;
    private String firstName;
    private String secondName;
    private String email;
    private Boolean active;
    private Boolean deleted;

}
