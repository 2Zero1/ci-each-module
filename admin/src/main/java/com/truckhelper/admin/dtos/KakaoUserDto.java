package com.truckhelper.admin.dtos;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class KakaoUserDto {
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccountDto kakaoAccount;

    private KakaoUserDto() {
    }

    public KakaoUserDto(String kakaoId, String phoneNumber) {
        this.id = kakaoId;
        this.kakaoAccount = new KakaoAccountDto(phoneNumber);
    }

    public static KakaoUserDto fake(String kakaoId, String phoneNumber) {
        return new KakaoUserDto(kakaoId, phoneNumber);
    }
}
