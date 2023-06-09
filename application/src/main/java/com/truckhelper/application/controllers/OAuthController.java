package com.truckhelper.application.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.truckhelper.application.applications.KakaoLoginService;
import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.dtos.OAuthTokenRequestDto;


@RestController
public class OAuthController {
    private final KakaoLoginService kakaoLoginService;

    public OAuthController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping("oauth/token")
    LoginResultDto token(
            @RequestBody OAuthTokenRequestDto oauthTokenRequestDto
    ) {
        String code = oauthTokenRequestDto.getCode();

        return kakaoLoginService.login(code);
    }
}
