package com.truckhelper.application.utils;

import org.springframework.web.client.RestTemplate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.truckhelper.core.models.GeoPosition;

class KakaoApiServiceTest {
    private KakaoApiService kakaoApiService;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = mock(RestTemplate.class);

        kakaoApiService = new KakaoApiService(
                "APP-KEY", "http://example.com/oauth/callback", restTemplate);
    }

    @Test
    void requestAccessToken() {
        String accessToken = kakaoApiService.requestAccessToken("TEST-CODE-3");

        assertThat(accessToken).isEqualTo("TEST-ACCESS-TOKEN-3");
    }

    @Test
    void fetchUser() {
        String userId = kakaoApiService.fetchUser("TEST-ACCESS-TOKEN-37");

        assertThat(userId).isEqualTo("37");
    }

    @Test
    void searchAddress() {
        GeoPosition position = kakaoApiService.searchAddress("서울");

        assertThat(position).isEqualTo(new GeoPosition(37.5665, 126.9780));
    }
}
