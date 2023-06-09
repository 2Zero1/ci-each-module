package com.truckhelper.core.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import com.github.f4b6a3.ulid.UlidCreator;

@Embeddable
@EqualsAndHashCode
public class NotificationId implements Serializable {
    @Column(name = "id")
    private String value;

    private NotificationId() {
    }

    public NotificationId(String value) {
        this.value = value;
    }

    public static NotificationId generate() {
        return new NotificationId(UlidCreator.getUlid().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
