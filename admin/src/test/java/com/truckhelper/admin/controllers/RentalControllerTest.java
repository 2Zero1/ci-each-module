package com.truckhelper.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.applications.RentalService;
import com.truckhelper.admin.dtos.RentalDto;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.repositories.RentalDtoFetcher;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;

@WebMvcTest(RentalController.class)
@ActiveProfiles("test")
public class RentalControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RentalService rentalService;

    @MockBean
    AdminRepository adminRepository;

    @MockBean
    RentalDtoFetcher rentalDtoFetcher;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /rentals")
    void list() throws Exception {
        RentalStatus status = RentalStatus.New;
        given(rentalDtoFetcher.fetchAll(status))
                .willReturn(List.of(RentalDto.fake()));

        mockMvc.perform(get("/rentals?status=new"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /rentals/{ID}?status={status}")
    void updateStatus() throws Exception {
        String id = "0001-RENTAL-ID";

        mockMvc.perform(patch("/rentals/" + id + "?status=" + "accepted"))
                .andExpect(status().isOk());

        verify(rentalService).updateStatus(
                any(RentalId.class), any(RentalStatus.class)
        );
    }
}
