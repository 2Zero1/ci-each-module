package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Address {
    private String address1;

    private String address2;

    private Address() {
    }

    public Address(String address1) {
        this.address1 = address1;
    }

    public Address(String address1, String address2) {
        this.address1 = address1;
        this.address2 = address2;
    }

    @Override
    public String toString() {
        if (address2 == null || address2.isBlank()) {
            return address1;
        }
        return address1 + " " + address2;
    }
}
