package com.truckhelper.application.applications;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

class UpdateUserServiceTest {
    private UserRepository userRepository;

    private UpdateUserService updateUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);

        updateUserService = new UpdateUserService(userRepository);
    }

    @Test
    void updateUser() {
        UserId userId = new UserId("tester");

        User user = User.fake(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Person person = Person.fake();
        Truck truck = Truck.fake();

        updateUserService.updateUser(userId, person, truck);

        assertThat(user.person()).isEqualTo(person);
        assertThat(user.truck()).isEqualTo(truck);
    }
}
