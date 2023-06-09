package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class GeoPosition {
    public static final GeoPosition SEOUL = new GeoPosition(37.5665, 126.9780);

    private Double latitude;

    private Double longitude;

    private GeoPosition() {
    }

    public GeoPosition(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
