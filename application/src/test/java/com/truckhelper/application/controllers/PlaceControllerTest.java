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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.dtos.PlaceDto;
import com.truckhelper.application.exceptions.PlaceNotFound;
import com.truckhelper.application.repositories.PlaceDtoFetcher;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;

@WebMvcTest(PlaceController.class)
@ActiveProfiles("test")
class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceDtoFetcher placeDtoFetcher;

    @MockBean
    private PlaceRepository placeRepository;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /places")
    void list() throws Exception {
        given(placeDtoFetcher.fetchAll(any(), any(), any()))
                .willReturn(List.of(PlaceDto.fake()));

        mockMvc.perform(get("/places"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"places\":[")
                ))
                .andExpect(content().string(
                        containsString("\"price\":150000")
                ));
    }

    @Test
    @DisplayName("GET /places/{id} when ID is correct")
    void detailWithCorrectId() throws Exception {
        PlaceId placeId = new PlaceId("0001PLACE");

        given(placeRepository.findById(placeId))
                .willReturn(Optional.of(Place.fake(placeId)));

        mockMvc.perform(get("/places/" + placeId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"place\":{")));
    }

    @Test
    @DisplayName("GET /places/{id} when ID is incorrect")
    void detailWithIncorrectId() throws Exception {
        String id = "xxx";

        PlaceId placeId = new PlaceId(id);

        given(placeRepository.findById(placeId))
                .willThrow(new PlaceNotFound(placeId));

        mockMvc.perform(get("/places/" + id))
                .andExpect(status().isNotFound());
    }
}
