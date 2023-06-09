package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PlaceImageDto {
    private String url;

    private PlaceImageDto() {
    }

    public PlaceImageDto(String url) {
        this.url = url;
    }
}
