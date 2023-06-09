package com.truckhelper.application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.applications.RentalListService;
import com.truckhelper.application.applications.ReservePlaceService;
import com.truckhelper.application.dtos.RentalDto;
import com.truckhelper.application.dtos.RentalsDto;
import com.truckhelper.application.dtos.ReserveDto;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.UserId;

@RestController
@RequestMapping("rentals")
public class RentalController {
    private final RentalListService rentalListService;

    private final ReservePlaceService reservePlaceService;

    private final ModelMapper modelMapper;

    public RentalController(RentalListService rentalListService,
                            ReservePlaceService reservePlaceService,
                            ModelMapper modelMapper) {
        this.rentalListService = rentalListService;
        this.reservePlaceService = reservePlaceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public RentalsDto list(
            @RequestAttribute UserId userId
    ) {
        List<Rental> rentals = rentalListService.list(userId);

        return new RentalsDto(
                rentals.stream()
                        .map(this::rentalToDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(
            @Valid @RequestBody ReserveDto reserveDto,
            @RequestAttribute UserId userId
    ) {
        reservePlaceService.reserve(userId, reserveDto);

        return "Reserved";
    }

    private RentalDto rentalToDto(Rental rental) {
        return modelMapper.map(rental, RentalDto.class);
    }
}
