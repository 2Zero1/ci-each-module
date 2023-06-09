package com.truckhelper.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.applications.KakaoLoginService;
import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.User;


@WebMvcTest(OAuthController.class)
@ActiveProfiles("test")
class OAuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /oauth/token")
    void oauthToken() throws Exception {
        User user = User.fake();

        given(kakaoLoginService.login("TEST-CODE"))
                .willReturn(new LoginResultDto("ACCESS-TOKEN"));

        mockMvc.perform(post("/oauth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEST-CODE\"}"))
                .andExpect(status().isOk());
    }
}
