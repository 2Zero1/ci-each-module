package com.truckhelper.application.dtos;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class KakaoOAuthTokenDto {
    @JsonProperty("access_token")
    private String accessToken;
}
