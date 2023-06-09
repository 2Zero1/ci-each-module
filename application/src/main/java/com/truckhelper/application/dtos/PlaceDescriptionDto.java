package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PlaceDescriptionDto {
    private String introduction;

    private String precautions;

    private PlaceDescriptionDto() {
    }

    public PlaceDescriptionDto(String introduction, String precautions) {
        this.introduction = introduction;
        this.precautions = precautions;
    }
}
