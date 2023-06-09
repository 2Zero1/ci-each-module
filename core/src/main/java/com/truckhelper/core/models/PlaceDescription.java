package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlaceDescription {
    private String introduction;

    private String precautions;

    public PlaceDescription() {
    }

    public PlaceDescription(String introduction, String precautions) {
        this.introduction = introduction;
        this.precautions = precautions;
    }
}
