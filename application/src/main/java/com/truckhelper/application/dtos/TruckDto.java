package com.truckhelper.application.dtos;

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
}
