package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PersonDto {
    private String name;

    private String phoneNumber;

    private String email;

    private AddressDto address;

    private GeoPositionDto position;
}
