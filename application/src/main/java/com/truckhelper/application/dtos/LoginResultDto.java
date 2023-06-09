package com.truckhelper.application.dtos;

import lombok.Getter;

import com.truckhelper.core.models.UserId;

@Getter
public class LoginResultDto {
    private String userId;

    private String accessToken;

    private LoginResultDto() {
    }

    public LoginResultDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResultDto(UserId userId, String accessToken) {
        this.userId = userId.toString();
        this.accessToken = accessToken;
    }
}
