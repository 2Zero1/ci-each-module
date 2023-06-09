package com.truckhelper.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;

@WebMvcTest(GreetingController.class)
@ActiveProfiles("test")
class GreetingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AdminRepository adminRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void greeting() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk());
    }
}
