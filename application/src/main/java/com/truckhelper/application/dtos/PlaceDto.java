package com.truckhelper.application.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceDto {
    private String id;

    private Double distance;

    private String name;

    private List<PlaceImageDto> images;

    private List<PlanDto> plans;

    private String address;

    private GeoPositionDto position;

    private PlaceDescriptionDto description;

    private PlaceSpacesDto spaces;

    private PlaceDto() {
    }

    @Builder
    public PlaceDto(String id, Double distance, String name,
                    List<PlaceImageDto> images, List<PlanDto> plans,
                    String address, GeoPositionDto position,
                    PlaceDescriptionDto description, PlaceSpacesDto spaces) {
        this.id = id;
        this.distance = distance;
        this.name = name;
        this.images = images;
        this.plans = plans;
        this.address = address;
        this.position = position;
        this.description = description;
        this.spaces = spaces;
    }

    public PlaceDto change(List<PlanDto> plans, Double distance) {
        this.plans = plans;
        this.distance = distance;
        return this;
    }

    public static PlaceDto fake() {
        return PlaceDto.builder()
                .id("0001PLACE")
                .plans(List.of(new PlanDto("monthly", 150_000L)))
                .build();
    }
}
