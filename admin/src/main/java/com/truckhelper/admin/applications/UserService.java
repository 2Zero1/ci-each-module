package com.truckhelper.admin.applications;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truckhelper.admin.exceptions.UserNotFound;
import com.truckhelper.admin.repositories.UserRepository;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository placeRepository) {
        this.userRepository = placeRepository;
    }

    public void updatePlace(UserId id, Person person, Truck truck) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id));

        user.changePerson(person);
        user.changeTruck(truck);
    }
}
