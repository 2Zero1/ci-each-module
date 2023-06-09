package com.truckhelper.application.applications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.application.utils.KakaoApiService;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;

class CreateUserServiceTest {
    private JwtUtil jwtUtil;

    private KakaoApiService kakaoApiService;

    private UserRepository userRepository;

    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("SECRET");

        kakaoApiService = mock(KakaoApiService.class);

        userRepository = mock(UserRepository.class);

        createUserService = new CreateUserService(
                jwtUtil, kakaoApiService, userRepository);
    }

    @Test
    void createUser() {
        String accessToken = "ACCESS-TOKEN-123";
        Person person = Person.fake();
        Truck truck = Truck.fake();

        given(kakaoApiService.fetchUser(accessToken)).willReturn("123");

        LoginResultDto loginResultDto = createUserService.createUser(
                accessToken, person, truck);

        assertThat(loginResultDto.getUserId()).isEqualTo("kakao-123");
        assertThat(loginResultDto.getAccessToken()).contains(".");

        verify(userRepository).save(any(User.class));
    }
}
