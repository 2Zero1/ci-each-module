package com.truckhelper.admin.dtos;

import lombok.Getter;

@Getter
public class LoginResultDto {
    private String accessToken;

    public LoginResultDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
