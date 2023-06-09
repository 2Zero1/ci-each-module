package com.truckhelper.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class EmailAddress {
    @Column(name = "email")
    private String value;

    private EmailAddress() {
    }

    public EmailAddress(String value) {
        this.value = value.strip();
    }

    @Override
    public String toString() {
        return value;
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }
}
