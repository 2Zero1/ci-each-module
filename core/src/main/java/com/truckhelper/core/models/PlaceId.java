package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import com.github.f4b6a3.ulid.UlidCreator;

@Embeddable
@EqualsAndHashCode
public class PlaceId implements Serializable {
    @Column(name = "id")
    private String value;

    private PlaceId() {
    }

    public PlaceId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PlaceId generate() {
        return new PlaceId(UlidCreator.getUlid().toString());
    }
}
