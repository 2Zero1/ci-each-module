package com.truckhelper.core.models;

import java.util.stream.Stream;

public enum RentalStatus {
    New("new"),
    Paid("paid"),
    Accepted("accepted"),
    Rejected("rejected"),
    Canceled("canceled");

    private final String value;

    RentalStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RentalStatus of(String value) {
        return Stream.of(RentalStatus.values())
                .filter(i -> i.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
