package com.truckhelper.application.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.application.dtos.PlaceDetailDto;
import com.truckhelper.application.dtos.PlaceDto;
import com.truckhelper.application.dtos.PlacesDto;
import com.truckhelper.application.dtos.PlanDto;
import com.truckhelper.application.exceptions.PlaceNotFound;
import com.truckhelper.application.repositories.PlaceDtoFetcher;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.Plan;
import com.truckhelper.core.models.PlanType;


@RestController
@RequestMapping("places")
public class PlaceController {
    private final PlaceDtoFetcher placeDtoFetcher;

    private final PlaceRepository placeRepository;

    private final ModelMapper modelMapper;

    public PlaceController(PlaceDtoFetcher placeDtoFetcher,
                           PlaceRepository placeRepository,
                           ModelMapper modelMapper) {
        this.placeDtoFetcher = placeDtoFetcher;
        this.placeRepository = placeRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    PlacesDto list(
            @RequestParam(value = "latitude", defaultValue = "37.28395")
            Double latitude,
            @RequestParam(value = "longitude", defaultValue = "127.1463")
            Double longitude,
            @RequestParam(value = "type", defaultValue = "")
            String planType
    ) {
        return new PlacesDto(
                placeDtoFetcher.fetchAll(latitude, longitude, planType)
        );
    }

    @GetMapping("{id}")
    PlaceDetailDto detail(
            @PathVariable String id,
            @RequestParam(value = "latitude", defaultValue = "37.28395")
            Double latitude,
            @RequestParam(value = "longitude", defaultValue = "127.1463")
            Double longitude
    ) {
        PlaceId placeId = new PlaceId(id);

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFound(placeId));

        List<Plan> plans = Stream.of(PlanType.Monthly, PlanType.Yearly)
                .map(place::plan)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Double distance = placeDtoFetcher
                .fetchDistance(placeId, latitude, longitude);

        return new PlaceDetailDto(placeToDto(place, plans, distance));
    }

    @ExceptionHandler(PlaceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not Found";
    }

    private PlaceDto placeToDto(Place place, List<Plan> plans,
                                Double distance) {
        PlaceDto placeDto = modelMapper.map(place, PlaceDto.class);
        return placeDto.change(
                plans.stream()
                        .map(plan -> modelMapper.map(plan, PlanDto.class))
                        .toList(),
                distance);
    }
}
