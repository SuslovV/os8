package ru.suslov.user_service.dto;

import lombok.Data;

@Data
public class BearerToken {

    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public BearerToken(String accessToken, String refreshToken, String tokenType) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
