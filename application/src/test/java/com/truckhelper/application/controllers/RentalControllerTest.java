package com.truckhelper.application.controllers;

import java.util.List;
import java.util.Optional;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.applications.RentalListService;
import com.truckhelper.application.applications.ReservePlaceService;
import com.truckhelper.application.dtos.ReserveDto;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.RentalRepository;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@WebMvcTest(RentalController.class)
@ActiveProfiles("test")
class RentalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalListService rentalListService;

    @MockBean
    private ReservePlaceService reservePlaceService;

    @MockBean
    private RentalRepository rentalRepository;

    @MockBean
    private PlaceRepository placeRepository;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /rentals (with logged in)")
    void list() throws Exception {
        UserId userId = new UserId("kakao-tester");
        String accessToken = jwtUtil.encode(userId);

        PlaceId placeId = new PlaceId("0001PLACE");

        given(userRepository.findById(userId))
                .willReturn(Optional.of(User.fake(userId)));

        given(rentalListService.list(userId))
                .willReturn(List.of(Rental.fake(placeId)));

        mockMvc.perform(get("/rentals")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"rentals\":[")
                ))
                .andExpect(content().string(
                        containsString("\"id\":\"RENTAL-ID\"")
                ));
    }

    @Test
    @DisplayName("GET /rentals (without logged in)")
    void listWithoutLoggedIn() throws Exception {
        mockMvc.perform(get("/rentals"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /rentals (with logged in)")
    void create() throws Exception {
        UserId userId = new UserId("kakao-tester");
        String accessToken = jwtUtil.encode(userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(User.fake(userId)));

        String json = """
                {
                    "placeId": "0001PLACE",
                    "method": "card",
                    "plan": "monthly",
                    "price": 300000,
                    "beginningDate": "2022-11-20",
                    "receiptId": "624e4f7c1fc19202e4746f91"
                }
                """;

        mockMvc.perform(post("/rentals")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(reservePlaceService)
                .reserve(any(UserId.class), any(ReserveDto.class));
    }

    @Test
    @DisplayName("POST /rentals (without logged in)")
    void createWithoutLoggedIn() throws Exception {
        UserId userId = new UserId("kakao-tester");
        String accessToken = jwtUtil.encode(userId);

        String json = """
                {
                    "placeId": "0001PLACE",
                    "method": "card",
                    "plan": "monthly",
                    "price": 300000,
                    "beginningDate": "2022-11-20",
                    "receiptId": "624e4f7c1fc19202e4746f91"
                }
                """;

        mockMvc.perform(post("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(reservePlaceService, never()).reserve(any(), any());
    }
}
