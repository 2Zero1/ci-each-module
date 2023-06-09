package com.truckhelper.admin.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceDto {
    private String id;

    private String name;

    private List<PlaceImageDto> images;

    private List<PlanDto> plans;

    private AddressDto address;

    private GeoPositionDto position;

    private PlaceDescriptionDto description;

    private PlaceSpacesDto spaces;

    private boolean hidden;

    private PlaceDto() {
    }

    @Builder
    public PlaceDto(
            String id,
            String name,
            List<PlaceImageDto> images,
            List<PlanDto> plans,
            AddressDto address,
            GeoPositionDto position,
            PlaceDescriptionDto description,
            PlaceSpacesDto spaces,
            boolean hidden
    ) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.plans = plans;
        this.address = address;
        this.position = position;
        this.description = description;
        this.spaces = spaces;
        this.hidden = hidden;
    }

    public PlaceDto change(List<PlanDto> plans) {
        this.plans = plans;
        return this;
    }
}
