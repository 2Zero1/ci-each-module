package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PlaceSpacesDto {
    private int total;

    private int free;

    private PlaceSpacesDto() {
    }

    public PlaceSpacesDto(int total, int free) {
        this.total = total;
        this.free = free;
    }
}
