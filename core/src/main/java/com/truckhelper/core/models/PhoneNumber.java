package com.truckhelper.core.models;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class PhoneNumber {
    @Column(name = "phone_number")
    private String value;

    private PhoneNumber() {
    }

    public PhoneNumber(String value) {
        this.value = value.replace("-", "").strip();
    }

    @Override
    public String toString() {
        return value;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
