package com.truckhelper.application.applications;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.application.utils.KakaoApiService;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@Service
public class KakaoLoginService {
    private final JwtUtil jwtUtil;

    private final KakaoApiService kakaoApiService;

    private final UserRepository userRepository;

    public KakaoLoginService(JwtUtil jwtUtil,
                             KakaoApiService kakaoApiService,
                             UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
        this.userRepository = userRepository;
    }

    public LoginResultDto login(String code) {
        String accessToken = kakaoApiService.requestAccessToken(code);
        String id = kakaoApiService.fetchUser(accessToken);

        UserId userId = new UserId("kakao-" + id);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return new LoginResultDto(accessToken);
        }

        return new LoginResultDto(userId, jwtUtil.encode(userId));
    }
}
