package com.truckhelper.core.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import lombok.Builder;

@Embeddable
public class Person {
    private String name;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private EmailAddress email;

    @Embedded
    private Address address;

    @Embedded
    private GeoPosition position;

    public Person() {
    }

    @Builder
    public Person(String name, PhoneNumber phoneNumber, EmailAddress email,
                  Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public static Person fake() {
        return Person.builder().build();
    }
}
