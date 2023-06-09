package com.truckhelper.admin.applications;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.truckhelper.admin.dtos.KakaoUserDto;
import com.truckhelper.admin.dtos.LoginResultDto;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.admin.utils.KakaoApiService;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.KakaoId;
import com.truckhelper.core.models.PhoneNumber;

@Service
@Transactional
public class KakaoLoginService {
    private KakaoApiService kakaoApiService;

    private AdminRepository adminRepository;

    private JwtUtil jwtUtil;

    public KakaoLoginService(KakaoApiService kakaoApiService,
                             AdminRepository adminRepository,
                             JwtUtil jwtUtil) {
        this.kakaoApiService = kakaoApiService;
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResultDto login(String code) {
        String accessToken = kakaoApiService.requestAccessToken(code);

        KakaoUserDto kakaoUserDto = kakaoApiService.fetchUser(accessToken);

        KakaoId kakaoId = new KakaoId(kakaoUserDto.getId());

        String phoneNumberValue = kakaoUserDto.getKakaoAccount().getPhoneNubmer();

        PhoneNumber phoneNumber = PhoneNumber.of(phoneNumberValue);

        Admin admin = adminRepository.findAdminByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Admin newAdmin = adminRepository.findByPhoneNumber(phoneNumber)
                            .orElseThrow(() -> new AuthenticationError());

                    newAdmin.changeKakaoId(kakaoId);

                    return newAdmin;
                });

        return new LoginResultDto(jwtUtil.encode(admin.id()));
    }
}
