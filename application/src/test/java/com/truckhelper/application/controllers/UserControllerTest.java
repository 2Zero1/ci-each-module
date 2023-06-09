package com.truckhelper.application.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.application.applications.CreateUserService;
import com.truckhelper.application.applications.UpdateUserService;
import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.exceptions.AccountAlreadyExists;
import com.truckhelper.application.exceptions.IncorrectAccessToken;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;


@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserService createUserService;

    @MockBean
    private UpdateUserService updateUserService;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    private String accessToken;

    @BeforeEach
    void setUp() {
        UserId userId = new UserId("kakao-tester");

        accessToken = jwtUtil.encode(userId);

        User user = User.fake(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
    }

    @Test
    @DisplayName("POST /users (with valid attributes)")
    void createSuccess() throws Exception {
        UserId userId = new UserId("kakao-37");

        given(createUserService.createUser(any(), any(), any()))
                .willReturn(new LoginResultDto(userId, "ACCESS-TOKEN"));

        String json = """
                {
                    "accessToken": "TEST-ACCESS-TOKEN-37",
                    "name": "Name",
                    "phoneNumber": "010-1111-2222",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "address2": "",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /users (with incorrect access token)")
    void createWithIncorrectAccessToken() throws Exception {
        given(createUserService.createUser(any(), any(), any()))
                .willThrow(new IncorrectAccessToken());

        String json = """
                {
                    "accessToken": "xxx",
                    "name": "Name",
                    "phoneNumber": "010-1111-2222",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users (with invalid attributes)")
    void createWithInvalidAttributes() throws Exception {
        String json = """
                {
                    "accessToken": "TEST-ACCESS-TOKEN-37",
                    "name": "",
                    "phoneNumber": "",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(createUserService, never()).createUser(any(), any(), any());
    }

    @Test
    @DisplayName("POST /users (when the user already exists)")
    void createWhenUserAlreadyExists() throws Exception {
        given(createUserService.createUser(any(), any(), any()))
                .willThrow(new AccountAlreadyExists());

        String json = """
                {
                    "accessToken": "TEST-ACCESS-TOKEN-1",
                    "name": "Name",
                    "phoneNumber": "010-1111-2222",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /users/me (with valid attributes)")
    void updateSuccess() throws Exception {
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
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(patch("/users/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /users/me (with invalid attributes)")
    void updateFail() throws Exception {
        String json = """
                {
                    "name": "",
                    "phoneNumber": "",
                    "email": "tester@example.com",
                    "address1": "Sing Street",
                    "vehiclePlate": "NUMBER",
                    "manufacturer": "KIA",
                    "carModel": "Super Truck",
                    "vehicleType": "Cargo",
                    "loadingWeight": "5t",
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(patch("/users/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(updateUserService, never()).updateUser(any(), any(), any());
    }

    @Test
    @DisplayName("PATCH /users/me (when user is not logged in)")
    void updateWithoutLogin() throws Exception {
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
                    "loadingLength": "5.5m",
                    "length": "7.2m",
                    "height": "2.5m",
                    "width": "2.35m"
                }
                """;

        mockMvc.perform(patch("/users/me")
                        .header("Authorization", "Bearer XXX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(updateUserService, never()).updateUser(any(), any(), any());
    }
}
