package com.truckhelper.admin.dtos;

import lombok.Getter;

@Getter
public class AddressDto {
    private String address1;

    private String address2;

    public AddressDto() {
    }

    public AddressDto(String address1, String address2) {
        this.address1 = address1;
        this.address2 = address2;
    }
}
