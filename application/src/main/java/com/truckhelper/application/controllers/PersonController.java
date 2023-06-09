package com.truckhelper.application.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.dtos.PersonDto;
import com.truckhelper.application.exceptions.AuthenticationError;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.User;


@CrossOrigin
@RestController
@RequestMapping("users/me/person")
public class PersonController {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public PersonController(UserRepository userRepository,
                            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    PersonDto detail(@RequestAttribute Optional<User> currentUser) {
        User user = currentUser.orElseThrow(AuthenticationError::new);
        return personToDto(user.person());
    }

    private PersonDto personToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }
}
