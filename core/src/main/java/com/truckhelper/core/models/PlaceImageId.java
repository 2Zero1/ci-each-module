package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import com.github.f4b6a3.ulid.UlidCreator;

@Embeddable
@EqualsAndHashCode
public class PlaceImageId implements Serializable {
    @Column(name = "id")
    private String value;

    private PlaceImageId() {
    }

    public PlaceImageId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PlaceImageId generate() {
        return new PlaceImageId(UlidCreator.getUlid().toString());
    }
}
