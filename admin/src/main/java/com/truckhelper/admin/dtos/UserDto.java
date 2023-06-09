package com.truckhelper.admin.dtos;

import lombok.Getter;

@Getter
public class UserDto {
    String id;

    TruckDto truck;

    PersonDto person;

    public UserDto() {
    }

    public UserDto(String id, TruckDto truck, PersonDto person) {
        this.id = id;
        this.truck = truck;
        this.person = person;
    }
}
