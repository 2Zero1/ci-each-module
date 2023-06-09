package com.truckhelper.application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {
    @Test
    @DisplayName("Load Spring Application Context")
    void contextLoads() {
    }
}
