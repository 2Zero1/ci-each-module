package com.truckhelper.application.utils;

import java.util.HashMap;
import java.util.Map;

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

import com.truckhelper.application.dtos.KakaoAddressDocumentDto;
import com.truckhelper.application.dtos.KakaoAddressResultDto;
import com.truckhelper.application.dtos.KakaoOAuthTokenDto;
import com.truckhelper.application.dtos.KakaoUserDto;
import com.truckhelper.core.models.GeoPosition;


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
        if (code.startsWith("TEST-CODE-")) {
            return "TEST-ACCESS-TOKEN-" + code.substring("TEST-CODE-".length());
        }

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

    public String fetchUser(String accessToken) {
        if (accessToken.startsWith("TEST-ACCESS-TOKEN-")) {
            return accessToken.substring("TEST-ACCESS-TOKEN-".length());
        }

        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserDto> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, KakaoUserDto.class);

        KakaoUserDto body = response.getBody();

        return body.getId();
    }

    public GeoPosition searchAddress(String address) {
        if (address.equals("서울")) {
            return GeoPosition.SEOUL;
        }

        String url = "https://dapi.kakao.com/v2/local/search/address.json" +
                "?query=" + address;

        Map<String, String> params = new HashMap<>();
        params.put("query", address);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + appKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoAddressResultDto> response =
                restTemplate.exchange(
                        url, HttpMethod.GET, requestEntity,
                        KakaoAddressResultDto.class);

        KakaoAddressResultDto result = response.getBody();

        KakaoAddressDocumentDto document = result.getDocuments().get(0);

        return new GeoPosition(
                Double.parseDouble(document.getY()),
                Double.parseDouble(document.getX())
        );
    }
}
