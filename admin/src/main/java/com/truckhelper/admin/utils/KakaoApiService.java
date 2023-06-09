package com.truckhelper.admin.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.truckhelper.admin.dtos.KakaoOAuthTokenDto;
import com.truckhelper.admin.dtos.KakaoUserDto;


@Service
public class KakaoApiService {
    private final String appKey;

    private final String redirectUri;

    private final RestTemplate restTemplate;

    public KakaoApiService(
            @Value("${kakao.appKey}") String appKey,
            @Value("${kakao.auth.redirectUrl}") String redirectUri,
            RestTemplate restTemplate) {
        this.appKey = appKey;
        this.redirectUri = redirectUri;
        this.restTemplate = restTemplate;
    }

    public String requestAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", appKey);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        KakaoOAuthTokenDto data = restTemplate.postForObject(
                url, formData, KakaoOAuthTokenDto.class);

        if (data == null) {
            return null;
        }

        return data.getAccessToken();
    }

    public KakaoUserDto fetchUser(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserDto> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, KakaoUserDto.class
        );

        return response.getBody();
    }
}
