package com.truckhelper.application.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.repositories.NotificationRepository;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.Notification;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@WebMvcTest(NotificationController.class)
@ActiveProfiles("test")
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /notifications")
    void list() throws Exception {
        UserId userId = new UserId("kakao-tester");

        given(userRepository.findById(userId))
                .willReturn(Optional.of(User.fake(userId)));

        given(notificationRepository.findAllByUserId(userId))
                .willReturn(List.of(Notification.fake()));

        String accessToken = jwtUtil.encode(userId);

        mockMvc.perform(get("/notifications")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"notifications\":[")
                ));
    }

    @Test
    @DisplayName("GET /notifications (with invalid accessToken)")
    void listWithInvalidAccessToken() throws Exception {
        mockMvc.perform(get("/notifications")
                        .header("Authorization", "Bearer XXX"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /notifications (when user is not logged in)")
    void listWithoutLogin() throws Exception {
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isBadRequest());
    }
}
