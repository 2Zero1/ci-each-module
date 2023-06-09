package com.truckhelper.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AdminApplicationTests {
    @Test
    @DisplayName("Load Spring Application Context")
    void contextLoads() {
    }
}
