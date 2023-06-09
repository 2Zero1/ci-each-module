package com.truckhelper.admin.dtos;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class KakaoAccountDto {
    @JsonProperty("phone_number")
    private String phoneNubmer;

    private KakaoAccountDto() {
    }

    public KakaoAccountDto(String phoneNubmer) {
        this.phoneNubmer = phoneNubmer;
    }
}
