package com.truckhelper.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackdoorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /backdoor/setup-database")
    void setupDatabase() throws Exception {
        mockMvc.perform(get("/backdoor/setup-database"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }

    @Test
    @DisplayName("GET /backdoor/clear-places")
    void clearPlaces() throws Exception {
        mockMvc.perform(get("/backdoor/clear-places"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }

    @Test
    @DisplayName("GET /backdoor/add-rental")
    void addRental() throws Exception {
        mockMvc.perform(get("/backdoor/add-rental" +
                        "?placeId=0001PLACE" +
                        "&plan=monthly" +
                        "&beginningDate=2022-09-25" +
                        "&status=paid"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }
}
