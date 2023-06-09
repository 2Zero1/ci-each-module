package com.truckhelper.core.models;

import java.util.stream.Stream;

public enum PayMethod {
    Card("card");

    private final String value;

    PayMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PayMethod of(String value) {
        return Stream.of(PayMethod.values())
                .filter(i -> i.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
