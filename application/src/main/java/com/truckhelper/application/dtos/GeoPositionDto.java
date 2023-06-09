package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class GeoPositionDto {
    private Double latitude;

    private Double longitude;

    private GeoPositionDto() {
    }

    public GeoPositionDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
