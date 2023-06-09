package com.truckhelper.application.applications;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@Service
@Transactional
public class UpdateUserService {
    private final UserRepository userRepository;

    public UpdateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateUser(UserId userId, Person person, Truck truck) {
        User user = userRepository.findById(userId).get();

        user.changePerson(person);
        user.changeTruck(truck);
    }
}
