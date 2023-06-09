package com.truckhelper.admin.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonDto {
    private String name;

    private String phoneNumber;

    private String email;

    private AddressDto address;

    private GeoPositionDto position;

    private PersonDto() {
    }

    @Builder
    public PersonDto(
            String name,
            String phoneNumber,
            String email,
            AddressDto address,
            GeoPositionDto position
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.position = position;
    }
}
