package com.truckhelper.application.controllers;

import java.util.Optional;

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
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@WebMvcTest(PersonController.class)
@ActiveProfiles("test")
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /users/me/person")
    void detail() throws Exception {
        UserId userId = new UserId("kakao-tester");

        String accessToken = jwtUtil.encode(userId);

        User user = User.fake(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        mockMvc.perform(get("/users/me/person")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
