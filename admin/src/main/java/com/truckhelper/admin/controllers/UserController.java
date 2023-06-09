package com.truckhelper.admin.controllers;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.admin.applications.UserService;
import com.truckhelper.admin.dtos.UserDetailDto;
import com.truckhelper.admin.dtos.UserDto;
import com.truckhelper.admin.dtos.UserUpdateDto;
import com.truckhelper.admin.dtos.UsersDto;
import com.truckhelper.admin.exceptions.UserNotFound;
import com.truckhelper.admin.repositories.UserRepository;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@CrossOrigin
@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    public UserController(
            UserService userService,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    UsersDto list(@RequestParam(value = "query", required = false)
                  String query) {
        List<User> users = query != null
                ? userRepository.findAll(query) : userRepository.findAll();

        List<UserDto> userDtos = users.stream().map(user -> userToDto(user))
                .collect(Collectors.toList());

        return new UsersDto(userDtos);
    }

    @GetMapping("{id}")
    UserDetailDto detail(@PathVariable String id) {
        UserId userId = new UserId(id);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound(userId));

        return new UserDetailDto(userToDto(user));
    }

    @PatchMapping("{id}")
    String update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        Person person = dtoToPerson(userUpdateDto);
        Truck truck = dtoToTruck(userUpdateDto);

        userService.updatePlace(new UserId(id), person, truck);

        return "Updated";
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not Found";
    }

    private UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private Person dtoToPerson(UserUpdateDto userUpdateDto) {
        return modelMapper.map(userUpdateDto, Person.class);
    }

    private Truck dtoToTruck(UserUpdateDto userUpdateDto) {
        return modelMapper.map(userUpdateDto, Truck.class);
    }
}
