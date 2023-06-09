package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class UserId implements Serializable {
    @Column(name = "id")
    private String value;

    private UserId() {
    }

    public UserId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
