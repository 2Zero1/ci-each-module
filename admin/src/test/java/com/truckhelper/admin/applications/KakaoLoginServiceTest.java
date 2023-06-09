package com.truckhelper.admin.applications;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.truckhelper.admin.dtos.KakaoUserDto;
import com.truckhelper.admin.dtos.LoginResultDto;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.admin.utils.KakaoApiService;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.KakaoId;
import com.truckhelper.core.models.PhoneNumber;

class KakaoLoginServiceTest {
    private KakaoLoginService kakaoLoginService;

    private KakaoApiService kakaoApiService;

    private AdminRepository adminRepository;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        kakaoApiService = mock(KakaoApiService.class);

        adminRepository = mock(AdminRepository.class);

        jwtUtil = new JwtUtil("SECRET");

        kakaoLoginService = new KakaoLoginService(
                kakaoApiService, adminRepository, jwtUtil
        );

        given(kakaoApiService.requestAccessToken("CODE"))
                .willReturn("ACCESS-TOKEN");
    }

    @Test
    @DisplayName("when new admin member logs in")
    void firstLogin() {
        given(kakaoApiService.fetchUser("ACCESS-TOKEN"))
                .willReturn(KakaoUserDto.fake("KAKAO-VALID-ID", "01012345678"));

        given(adminRepository.findAdminByKakaoId(new KakaoId("KAKAO-VALID-ID")))
                .willReturn(Optional.empty());

        given(adminRepository.findByPhoneNumber(PhoneNumber.of("01012345678")))
                .willReturn(Optional.of(Admin.fake("01012345678")));

        LoginResultDto result = kakaoLoginService.login("CODE");

        assertThat(result.getAccessToken()).isNotNull();

        verify(adminRepository).findAdminByKakaoId(any());
        verify(adminRepository).findByPhoneNumber(any());
    }

    @Test
    @DisplayName("when admin member logs in")
    void login() {
        given(kakaoApiService.fetchUser("ACCESS-TOKEN"))
                .willReturn(KakaoUserDto.fake("KAKAO-VALID-ID", "01012345678"));

        given(adminRepository.findAdminByKakaoId(new KakaoId("KAKAO-VALID-ID")))
                .willReturn(Optional.of(Admin.fake("01012345678")));

        LoginResultDto result = kakaoLoginService.login("CODE");

        assertThat(result.getAccessToken()).isNotNull();

        verify(adminRepository).findAdminByKakaoId(any());
        verify(adminRepository, never()).findByPhoneNumber(any());
    }

    @Test
    @DisplayName("When logs in with unauthorized account")
    void loginWithUnregisteredAccount() {
        given(kakaoApiService.fetchUser("ACCESS-TOKEN"))
                .willReturn(KakaoUserDto.fake("KAKAO-INVALID-ID", "01000000000"));

        given(adminRepository.findAdminByKakaoId(new KakaoId("KAKAO-VALID-ID")))
                .willReturn(Optional.empty());

        given(adminRepository.findByPhoneNumber(PhoneNumber.of("01000000000")))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> kakaoLoginService.login("CODE"))
                .isInstanceOf(AuthenticationError.class);
    }
}
