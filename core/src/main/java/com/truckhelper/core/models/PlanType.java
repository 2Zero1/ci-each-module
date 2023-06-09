package com.truckhelper.core.models;

import java.util.stream.Stream;

public enum PlanType {
    Monthly("monthly"),
    Yearly("yearly");

    private final String value;

    PlanType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PlanType of(String value) {
        return Stream.of(PlanType.values())
                .filter(i -> i.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
