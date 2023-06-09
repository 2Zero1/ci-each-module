package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import lombok.Builder;

@Embeddable
public class Truck {
    private String vehiclePlate;

    private String manufacturer;

    private String carModel;

    private String vehicleType;

    private String loadingWeight;

    private String loadingLength;

    @Embedded
    private Size size;

    private Truck() {
    }

    @Builder
    public Truck(String vehiclePlate, String manufacturer, String carModel,
                 String vehicleType, String loadingWeight, String loadingLength,
                 Size size) {
        this.vehiclePlate = vehiclePlate;
        this.manufacturer = manufacturer;
        this.carModel = carModel;
        this.vehicleType = vehicleType;
        this.loadingWeight = loadingWeight;
        this.loadingLength = loadingLength;
        this.size = size;
    }

    public static Truck fake() {
        return Truck.builder().build();
    }
}
