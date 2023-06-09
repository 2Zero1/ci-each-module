package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import com.github.f4b6a3.ulid.UlidCreator;

@Embeddable
@EqualsAndHashCode
public class AdminId implements Serializable {
    @Column(name = "id")
    private String value;

    private AdminId() {
    }

    public AdminId(String value) {
        this.value = value;
    }

    public static AdminId generate() {
        return new AdminId(UlidCreator.getUlid().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
