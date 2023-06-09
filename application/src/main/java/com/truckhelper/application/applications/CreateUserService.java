package com.truckhelper.application.applications;

import org.springframework.stereotype.Service;

import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.exceptions.AccountAlreadyExists;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.application.utils.KakaoApiService;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@Service
public class CreateUserService {
    private JwtUtil jwtUtil;

    private KakaoApiService kakaoApiService;

    private UserRepository userRepository;

    public CreateUserService(JwtUtil jwtUtil,
                             KakaoApiService kakaoApiService,
                             UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
        this.userRepository = userRepository;
    }

    public LoginResultDto createUser(
            String accessToken, Person person, Truck truck) {
        String id = kakaoApiService.fetchUser(accessToken);

        UserId userId = new UserId("kakao-" + id);

        if (userRepository.existsById(userId)) {
            throw new AccountAlreadyExists();
        }

        User user = new User(userId, person, truck);

        userRepository.save(user);

        return new LoginResultDto(userId, jwtUtil.encode(userId));
    }
}
