package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import com.github.f4b6a3.ulid.UlidCreator;

@Embeddable
@EqualsAndHashCode
public class RentalId implements Serializable {
    @Column(name = "id")
    private String value;

    private RentalId() {
    }

    public RentalId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RentalId generate() {
        return new RentalId(UlidCreator.getUlid().toString());
    }
}
