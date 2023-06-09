package com.truckhelper.core.models;

import java.util.stream.Stream;

public enum Grade {
    MASTER("MASTER"), LV1("LV1");

    private String value;

    Grade(String value) {
        this.value = value;
    }

    public static Grade of(String value) {
        return Stream.of(Grade.values())
                .filter(i -> i.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return value;
    }
}
