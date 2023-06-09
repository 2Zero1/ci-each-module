package com.truckhelper.admin.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.modelmapper.ModelMapper;

import com.truckhelper.admin.applications.PlaceService;
import com.truckhelper.admin.dtos.ImageOrderUpdateDto;
import com.truckhelper.admin.dtos.PlaceCreateDto;
import com.truckhelper.admin.dtos.PlaceDetailDto;
import com.truckhelper.admin.dtos.PlaceDto;
import com.truckhelper.admin.dtos.PlaceUpdateDto;
import com.truckhelper.admin.dtos.PlacesDto;
import com.truckhelper.admin.dtos.PlanDto;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.exceptions.PlaceImageNotFound;
import com.truckhelper.admin.exceptions.PlaceNotFound;
import com.truckhelper.admin.repositories.PlaceImageRepository;
import com.truckhelper.admin.repositories.PlaceRepository;
import com.truckhelper.core.models.Address;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.GeoPosition;
import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceDescription;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlaceImageId;
import com.truckhelper.core.models.PlaceSpaces;
import com.truckhelper.core.models.Plan;
import com.truckhelper.core.models.PlanType;

@CrossOrigin
@RestController
@RequestMapping("places")
public class PlaceController {
    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    private final ModelMapper modelMapper;

    public PlaceController(
            PlaceService placeService,
            PlaceRepository placeRepository,
            PlaceImageRepository placeImageRepository,
            ModelMapper modelMapper
    ) {
        this.placeService = placeService;
        this.placeRepository = placeRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("{id}/image")
    @ResponseStatus(HttpStatus.CREATED)
    String uploadImage(
            @RequestAttribute Optional<Admin> currentAdmin,
            @RequestParam("file") MultipartFile file,
            @PathVariable String id
    ) {
        currentAdmin.orElseThrow(AuthenticationError::new);

        placeService.uploadImage(new PlaceId(id), file);

        return "Created";
    }

    @DeleteMapping("image/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteImage(
            @RequestAttribute Optional<Admin> currentAdmin,
            @PathVariable String id
    ) {
        currentAdmin.orElseThrow(AuthenticationError::new);

        placeService.deleteImage(new PlaceImageId(id));

        return "Deleted";
    }

    @PatchMapping("{placeId}/image/order")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String updateImageOrder(
            @RequestAttribute Optional<Admin> currentAdmin,
            @PathVariable String placeId,
            @Valid @RequestBody ImageOrderUpdateDto imageOrderUpdateDto
    ) {
        currentAdmin.orElseThrow(AuthenticationError::new);

        placeService.updateImageOrder(
                new PlaceId(placeId),
                imageOrderUpdateDto.getFrom(),
                imageOrderUpdateDto.getTo()
        );

        return "Updated";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    String create(@Valid @RequestBody PlaceCreateDto placeCreateDto) {
        Place place = dtoToPlace(placeCreateDto);

        placeService.createPlace(place);

        return "Created";
    }

    @PatchMapping
    String update(@Valid @RequestBody PlaceUpdateDto placeUpdateDto) {
        placeService.updatePlace(
                new PlaceId(placeUpdateDto.getId()),
                placeUpdateDto.getName(),
                dtoToAddress(placeUpdateDto),
                dtoToPosition(placeUpdateDto),
                dtoToDescription(placeUpdateDto),
                dtoToSpaces(placeUpdateDto),
                dtoToPlan(placeUpdateDto.getPlans()),
                placeUpdateDto.isHidden()
        );
        return "Updated";
    }

    @GetMapping
    PlacesDto list(@RequestParam(value = "query", required = false)
                   String query) {
        List<Place> places = query != null
                ? placeRepository.findAll(query) : placeRepository.findAll();
        List<PlaceDto> placeDtos = places.stream().map(place ->
                        placeToDto(place, getPlans(place)))
                .collect(Collectors.toList());

        return new PlacesDto(placeDtos);
    }

    @GetMapping("{id}")
    PlaceDetailDto detail(@PathVariable String id) {
        PlaceId placeId = new PlaceId(id);

        Place place = placeRepository.findPlaceWithNonDeletedImages(placeId)
                .orElseThrow(() -> new PlaceNotFound(placeId));

        PlaceDto placeDto = placeToDto(place, getPlans(place));

        return new PlaceDetailDto(placeDto);
    }

    @ExceptionHandler(PlaceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "Not Found";
    }

    @ExceptionHandler(PlaceImageNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String placeImageNotFound() {
        return "Not Found";
    }

    private List<Plan> getPlans(Place place) {
        return Stream.of(PlanType.Monthly, PlanType.Yearly)
                .map(place::plan)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private PlaceDto placeToDto(Place place, List<Plan> plans) {
        PlaceDto placeDto = this.modelMapper.map(place, PlaceDto.class);

        return placeDto.change(plans.stream()
                .map(plan -> modelMapper.map(plan, PlanDto.class))
                .toList());
    }

    private Place dtoToPlace(PlaceCreateDto place) {
        return Place.builder()
                .id(PlaceId.generate())
                .name(place.getName())
                .address(modelMapper.map(place, Address.class))
                .position(modelMapper.map(place, GeoPosition.class))
                .description(modelMapper.map(place, PlaceDescription.class))
                .spaces(modelMapper.map(place, PlaceSpaces.class))
                .plans(dtoToPlan(place.getPlans()))
                .hidden(place.isHidden())
                .build();
    }

    private PlaceSpaces dtoToSpaces(PlaceUpdateDto place) {
        return modelMapper.map(place, PlaceSpaces.class);
    }

    private Address dtoToAddress(PlaceUpdateDto place) {
        return modelMapper.map(place, Address.class);
    }

    private PlaceDescription dtoToDescription(PlaceUpdateDto place) {
        return modelMapper.map(place, PlaceDescription.class);
    }

    private GeoPosition dtoToPosition(PlaceUpdateDto place) {
        return modelMapper.map(place, GeoPosition.class);
    }

    private List<Plan> dtoToPlan(List<PlanDto> plans) {
        return plans.stream().map((plan) -> new Plan(
                PlanType.of(plan.getType()),
                Money.krw(plan.getPrice())
        )).collect(Collectors.toList());
    }
}
