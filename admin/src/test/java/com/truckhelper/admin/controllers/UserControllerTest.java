package com.truckhelper.admin.controllers;

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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.applications.UserService;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.repositories.UserRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AdminRepository adminRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /users")
    void list() throws Exception {
        given(userRepository.findAll())
                .willReturn(List.of(User.fake(new UserId("0001-ID"))));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"users\":[")));
    }

    @Test
    @DisplayName("GET /users/{id}")
    void detail() throws Exception {
        String userId = "0001-ID";

        given(userRepository.findById(new UserId("0001-ID")))
                .willReturn(Optional.of(User.fake(new UserId("0001-ID"))));

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"user\":{")));
    }

    @Test
    @DisplayName("PATCH /users/{id}")
    void update() throws Exception {
        String userId = "0001-ID";

        String json = """
                {
                    "name": "New Name",
                    "phoneNumber": "010-1111-2222",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "address2": "",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(patch("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(userService).updatePlace(
                any(UserId.class), any(Person.class), any(Truck.class)
        );
    }
}
