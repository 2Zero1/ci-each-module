package com.truckhelper.admin.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.truckhelper.admin.applications.KakaoLoginService;
import com.truckhelper.admin.dtos.LoginResultDto;
import com.truckhelper.admin.dtos.OAuthTokenRequestDto;

@CrossOrigin
@RestController
public class OAuthController {
    private KakaoLoginService kakaoLoginService;

    public OAuthController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping("oauth/token")
    public LoginResultDto token(
            @RequestBody OAuthTokenRequestDto oAuthTokenRequestDto
    ) {
        String code = oAuthTokenRequestDto.getCode();

        return kakaoLoginService.login(code);
    }


}
