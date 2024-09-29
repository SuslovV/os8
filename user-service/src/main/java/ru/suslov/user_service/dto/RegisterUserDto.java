package ru.suslov.user_service.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

    private String username;
    private String firstName;
    private String secondName;
    private String email;
    private String password;

}
