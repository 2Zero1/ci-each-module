package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Size {
    private String length;

    private String height;

    private String width;

    public Size() {
    }

    @Builder
    public Size(String length, String height, String width) {
        this.length = length;
        this.height = height;
        this.width = width;
    }
}
