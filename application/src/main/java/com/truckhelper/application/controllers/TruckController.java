package com.truckhelper.application.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.dtos.TruckDto;
import com.truckhelper.application.exceptions.AuthenticationError;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;

@CrossOrigin
@RestController
@RequestMapping("users/me/truck")
public class TruckController {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public TruckController(UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    TruckDto detail(@RequestAttribute Optional<User> currentUser) {
        User user = currentUser.orElseThrow(AuthenticationError::new);

        return truckToDto(user.truck());
    }

    private TruckDto truckToDto(Truck truck) {
        return modelMapper.map(truck, TruckDto.class);
    }
}
