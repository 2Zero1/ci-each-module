package com.truckhelper.admin.dtos;

import lombok.Getter;

@Getter
public class PlaceImageDto {
    private String id;

    private String url;

    private PlaceImageDto() {
    }

    public PlaceImageDto(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
