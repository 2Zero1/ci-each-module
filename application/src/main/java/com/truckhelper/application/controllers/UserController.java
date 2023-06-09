package com.truckhelper.application.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.applications.CreateUserService;
import com.truckhelper.application.applications.UpdateUserService;
import com.truckhelper.application.dtos.LoginResultDto;
import com.truckhelper.application.dtos.UserCreateDto;
import com.truckhelper.application.dtos.UserUpdateDto;
import com.truckhelper.application.exceptions.AccountAlreadyExists;
import com.truckhelper.application.exceptions.AuthenticationError;
import com.truckhelper.application.exceptions.IncorrectAccessToken;
import com.truckhelper.core.models.Person;
import com.truckhelper.core.models.Truck;
import com.truckhelper.core.models.UserId;


@CrossOrigin
@RestController
@RequestMapping("users")
public class UserController {
    private final CreateUserService createUserService;

    private final UpdateUserService updateUserService;

    private final ModelMapper modelMapper;

    public UserController(CreateUserService createUserService,
                          UpdateUserService updateUserService,
                          ModelMapper modelMapper) {
        this.createUserService = createUserService;
        this.updateUserService = updateUserService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LoginResultDto create(
            @Valid @RequestBody UserCreateDto userCreateDto
    ) {
        String accessToken = userCreateDto.getAccessToken();
        Person person = dtoToPerson(userCreateDto);
        Truck truck = dtoToTruck(userCreateDto);

        return createUserService.createUser(accessToken, person, truck);
    }

    @PatchMapping("me")
    String update(
            @Valid @RequestBody UserUpdateDto userUpdateDto,
            @RequestAttribute UserId userId
    ) {
        if (userId == null) {
            throw new AuthenticationError();
        }

        Person person = dtoToPerson(userUpdateDto);
        Truck truck = dtoToTruck(userUpdateDto);

        updateUserService.updateUser(userId, person, truck);

        return "Updated";
    }

    @ExceptionHandler(AccountAlreadyExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String accountAlreadyExists() {
        return "Account Already Exists";
    }

    @ExceptionHandler(IncorrectAccessToken.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String incorrectAccessToken() {
        return "Incorrect Access Token";
    }

    private Person dtoToPerson(UserCreateDto userCreateDto) {
        return modelMapper.map(userCreateDto, Person.class);
    }

    private Person dtoToPerson(UserUpdateDto userUpdateDto) {
        return modelMapper.map(userUpdateDto, Person.class);
    }

    private Truck dtoToTruck(UserCreateDto userCreateDto) {
        return modelMapper.map(userCreateDto, Truck.class);
    }

    private Truck dtoToTruck(UserUpdateDto userUpdateDto) {
        return modelMapper.map(userUpdateDto, Truck.class);
    }
}
