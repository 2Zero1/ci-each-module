package com.truckhelper.admin.applications;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.truckhelper.admin.dtos.AdminCreateDto;
import com.truckhelper.admin.dtos.AdminUpdateDto;
import com.truckhelper.admin.exceptions.AdminNotFound;
import com.truckhelper.admin.exceptions.DuplicatePhoneNumberError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.PhoneNumber;


class AdminServiceTest {
    private AdminService adminService;

    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        adminRepository = mock(AdminRepository.class);

        adminService = new AdminService(adminRepository);
    }

    @Test
    void create() {
        given(adminRepository.findByPhoneNumber(PhoneNumber.of("01012345678")))
                .willReturn(Optional.empty());

        adminService.create(AdminCreateDto.fake("01012345678"));

        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void createWithDuplicatePhoneNumber() {
        given(adminRepository.findByPhoneNumber(PhoneNumber.of("01012345678")))
                .willReturn(Optional.of(Admin.fake("01012345678")));

        assertThatThrownBy(() -> adminService.create(AdminCreateDto.fake("01012345678")))
                .isInstanceOf(DuplicatePhoneNumberError.class);

        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void update() {
        AdminId id = new AdminId("ADMIN-ID-01");

        Admin admin = Admin.fake("ADMIN-ID-01", "01012345678");

        given(adminRepository.findById(id)).willReturn(Optional.of(admin));

        Admin updatedAdmin = adminService.update(
                id,
                AdminUpdateDto.fake("01000000000")
        );

        assertThat(updatedAdmin.phoneNumber())
                .isEqualTo(PhoneNumber.of("01000000000"));

        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void updateWithDuplicatePhoneNumber() {
        AdminId id = new AdminId("ADMIN-ID-01");

        Admin admin = Admin.fake("ADMIN-ID-01", "01012345678");

        given(adminRepository.findById(id)).willReturn(Optional.of(admin));

        given(adminRepository.findByPhoneNumber(PhoneNumber.of("01000000000")))
                .willReturn(Optional.of(Admin.fake("01000000000")));

        assertThatThrownBy(() -> adminService.update(
                id,
                AdminUpdateDto.fake("01000000000")))
                .isInstanceOf(DuplicatePhoneNumberError.class);
    }

    @Test
    void updateWithNonexistentAdmin() {
        AdminId id = new AdminId("NON-EXISTENT-ADMIN-ID-01");

        given(adminRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.update(
                id,
                AdminUpdateDto.fake("01000000000"))
        ).isInstanceOf(AdminNotFound.class);

        verify(adminRepository, never()).save(any(Admin.class));
    }
}
