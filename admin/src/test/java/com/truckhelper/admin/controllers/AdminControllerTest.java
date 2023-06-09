package com.truckhelper.admin.controllers;

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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.truckhelper.admin.applications.AdminService;
import com.truckhelper.admin.dtos.AdminCreateDto;
import com.truckhelper.admin.dtos.AdminUpdateDto;
import com.truckhelper.admin.exceptions.AdminNotFound;
import com.truckhelper.admin.exceptions.DuplicatePhoneNumberError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.Grade;
import com.truckhelper.core.models.PhoneNumber;

@WebMvcTest(AdminController.class)
@ActiveProfiles("test")
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    AdminRepository adminRepository;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /admins when authorized admin request")
    void listFromAuthorized() throws Exception {
        AdminId adminId = new AdminId("authorized-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        mockMvc.perform(get("/admins")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"admins\":[")));
    }

    @Test
    @DisplayName("GET /admins/me when authenticated access")
    void getFromAuthenticated() throws Exception {
        AdminId adminId = new AdminId("authorized-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        mockMvc.perform(get("/admins/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"admin\":{")));
    }

    @Test
    @DisplayName("GET /admins when unauthenticated admin request")
    void listFromUnauthenticated() throws Exception {
        AdminId adminId = new AdminId("unauthenticated-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/admins")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /admins/me when unauthenticated access")
    void getFromUnauthenticated() throws Exception {
        AdminId adminId = new AdminId("unauthenticated-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/admins/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /admins when authorized admin creates new admin")
    void createFromAuthorized() throws Exception {
        AdminId adminId = new AdminId("authorized-id");
        String accessToken = jwtUtil.encode(adminId);

        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": false
                }
                """;

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        mockMvc.perform(post("/admins")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(adminService).create(any(AdminCreateDto.class));
    }

    @Test
    @DisplayName("PATCH /admins when authorized admin update")
    void updateFromAuthorized() throws Exception {
        AdminId topLevelAdminId = new AdminId("TOP-LEVEL-ADMIN-ID");
        AdminId targetAdminId = new AdminId("TARGET-ADMIN-ID");

        String accessToken = jwtUtil.encode(topLevelAdminId);


        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": "false"
                }
                """;

        given(adminRepository.findById(topLevelAdminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        given(adminRepository.findById(targetAdminId))
                .willReturn(Optional.of(Admin.fake("01099998888", Grade.LV1)));

        mockMvc.perform(patch("/admins/TARGET-ADMIN-ID")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(adminService).update(any(AdminId.class), any(AdminUpdateDto.class));
    }

    @Test
    @DisplayName("PATCH /admins with duplicate phonenumber")
    void updateWithDuplicatedPhoneNumber() throws Exception {
        AdminId topLevelAdminId = new AdminId("TOP-LEVEL-ADMIN-ID");
        AdminId targetAdminId = new AdminId("TARGET-ADMIN-ID");

        String accessToken = jwtUtil.encode(topLevelAdminId);


        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": "false"
                }
                """;

        given(adminRepository.findById(topLevelAdminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        given(adminService.update(any(AdminId.class), any(AdminUpdateDto.class)))
                .willThrow(new DuplicatePhoneNumberError(
                        PhoneNumber.of("01012345678")));

        mockMvc.perform(patch("/admins/TARGET-ADMIN-ID")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /admins when update top level admin")
    void updateTopLevelAdmin() throws Exception {
        AdminId topLevelAdminId = new AdminId("TOP-LEVEL-ADMIN-ID");

        String accessToken = jwtUtil.encode(topLevelAdminId);


        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": "false"
                }
                """;

        given(adminRepository.findById(topLevelAdminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        given(adminRepository.findById(topLevelAdminId))
                .willReturn(Optional.of(Admin.fake("01099998888", Grade.LV1)));

        mockMvc.perform(patch("/admins/TARGET-ADMIN-ID")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PATCH /admins when admin does not exist")
    void updateNonexistentAdmin() throws Exception {
        AdminId topLevelAdminId = new AdminId("TOP-LEVEL-ADMIN-ID");
        AdminId targetAdminId = new AdminId("NONEXISTENT-TARGET-ADMIN-ID");

        String accessToken = jwtUtil.encode(topLevelAdminId);

        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": "false"
                }
                """;

        given(adminRepository.findById(topLevelAdminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        given(adminService.update(any(AdminId.class), any(AdminUpdateDto.class)))
                .willThrow(new AdminNotFound(targetAdminId));

        mockMvc.perform(patch("/admins/NONEXISTENT-TARGET-ADMIN-ID")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /admins when unauthorized admin update")
    void updateFromUnauthorized() throws Exception {
        AdminId adminId = new AdminId("NOT-TOP-LEVEL-ADMIN-ID");
        AdminId targetAdminId = new AdminId("TARGET-ADMIN-ID");

        String accessToken = jwtUtil.encode(adminId);

        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678",
                    "inactive": "false"
                }
                """;

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.LV1)));

        given(adminRepository.findById(targetAdminId))
                .willReturn(Optional.of(Admin.fake("01099998888", Grade.LV1)));

        mockMvc.perform(patch("/admins/TARGET-ADMIN-ID")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /admins when unauthorized admin request")
    void listFromUnauthorized() throws Exception {
        AdminId adminId = new AdminId("unauthorized-id");
        String accessToken = jwtUtil.encode(adminId);

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01056781234", Grade.LV1)));

        mockMvc.perform(get("/admins")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /admins when unauthorized admin creates new admin")
    void createFromUnauthorized() throws Exception {
        AdminId adminId = new AdminId("unauthorized-id");
        String accessToken = jwtUtil.encode(adminId);

        String json = """
                {
                    "name": "jack",
                    "phoneNumber": "01056781234",
                    "inactive": false
                }
                """;

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01056781234", Grade.LV1)));

        mockMvc.perform(post("/admins")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /admin when unauthenticated user creates new admin")
    void createFromUnauthenticated() throws Exception {
        AdminId adminId = new AdminId("unauthenticated-id");
        String accessToken = jwtUtil.encode(adminId);

        String json = """
                {
                    "name": "luffin",
                    "phoneNumber": "01000000000",
                    "inactive": false
                }
                """;

        given(adminRepository.findById(adminId))
                .willReturn(Optional.empty());

        mockMvc.perform(post("/admins")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /admins when phone number is duplicate")
    void createWithDuplicatePhoneNumber() throws Exception {
        AdminId adminId = new AdminId("authorized-id");
        String accessToken = jwtUtil.encode(adminId);

        String json = """
                {
                    "name": "gildong",
                    "phoneNumber": "01012345678"
                    "inactive": false
                }
                """;

        given(adminRepository.findById(adminId))
                .willReturn(Optional.of(Admin.fake("01012345678", Grade.MASTER)));

        doThrow(new DuplicatePhoneNumberError(PhoneNumber.of("01012345678")))
                .when(adminService).create(any(AdminCreateDto.class));

        mockMvc.perform(post("/admins")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
