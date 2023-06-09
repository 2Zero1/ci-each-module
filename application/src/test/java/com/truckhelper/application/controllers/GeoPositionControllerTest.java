package com.truckhelper.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.application.utils.KakaoApiService;
import com.truckhelper.core.models.GeoPosition;


@WebMvcTest(GeoPositionController.class)
@ActiveProfiles("test")
class GeoPositionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoApiService kakaoApiService;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /geo-position")
    void search() throws Exception {
        given(kakaoApiService.searchAddress("서울시"))
                .willReturn(GeoPosition.SEOUL);

        mockMvc.perform(get("/geo-position?address=서울시"))
                .andExpect(status().isOk());
    }
}
