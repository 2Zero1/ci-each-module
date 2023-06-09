package com.truckhelper.admin.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.admin.applications.RentalService;
import com.truckhelper.admin.dtos.RentalDto;
import com.truckhelper.admin.dtos.RentalsDto;
import com.truckhelper.admin.exceptions.RentalNotFound;
import com.truckhelper.admin.repositories.RentalDtoFetcher;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;

@CrossOrigin
@RestController
@RequestMapping("rentals")
public class RentalController {
    private ModelMapper modelMapper;

    private RentalService rentalService;

    private RentalDtoFetcher rentalDtoFetcher;

    public RentalController(
            ModelMapper modelMapper,
            RentalService rentalService,
            RentalDtoFetcher rentalDtoFetcher
    ) {
        this.modelMapper = modelMapper;
        this.rentalService = rentalService;
        this.rentalDtoFetcher = rentalDtoFetcher;
    }

    @GetMapping
    RentalsDto list(
            @RequestParam(value = "status", defaultValue = "paid")
            String status
    ) {
        List<RentalDto> rentals = rentalDtoFetcher.fetchAll(RentalStatus.of(status));

        return new RentalsDto(rentals);
    }

    @PatchMapping("{id}")
    String update(
            @PathVariable String id,
            @RequestParam(value = "status") String status
    ) {
        rentalService.updateStatus(new RentalId(id), RentalStatus.of(status));
        return "Updated";
    }

    @ExceptionHandler(RentalNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not Found";
    }

    private RentalDto rentalToDto(Rental rental) {
        return modelMapper.map(rental, RentalDto.class);
    }
}
