package com.truckhelper.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.applications.KakaoLoginService;
import com.truckhelper.admin.dtos.LoginResultDto;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;

@WebMvcTest(OAuthController.class)
@ActiveProfiles("test")
class OAuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @MockBean
    AdminRepository adminRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /oauth/token")
    void token() throws Exception {
        given(kakaoLoginService.login("VALID-CODE"))
                .willReturn(new LoginResultDto("ACCESS-TOKEN"));

        mockMvc.perform(post("/oauth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"VALID-CODE\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"accessToken\":")));
    }

    @Test
    @DisplayName("POST /oauth/token (invalid access)")
    void invalidAccess() throws Exception {
        given(kakaoLoginService.login("INVALID-CODE"))
                .willThrow(AuthenticationError.class);

        mockMvc.perform(post("/oauth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"INVALID-CODE\"}"))
                .andExpect(status().isForbidden());
    }
}
