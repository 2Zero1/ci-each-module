package com.truckhelper.admin.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TruckDto {
    private String vehiclePlate;

    private String manufacturer;

    private String carModel;

    private String vehicleType;

    private String loadingWeight;

    private String loadingLength;

    private SizeDto size;

    private TruckDto() {
    }

    @Builder
    public TruckDto(
            String vehiclePlate,
            String manufacturer,
            String carModel,
            String vehicleType,
            String loadingWeight,
            String loadingLength,
            SizeDto size
    ) {
        this.vehiclePlate = vehiclePlate;
        this.manufacturer = manufacturer;
        this.carModel = carModel;
        this.vehicleType = vehicleType;
        this.loadingWeight = loadingWeight;
        this.loadingLength = loadingLength;
        this.size = size;
    }
}
