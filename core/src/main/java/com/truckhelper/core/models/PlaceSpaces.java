package com.truckhelper.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PlaceSpaces {
    @Column(name = "total_spaces")
    private int total;

    @Column(name = "free_spaces")
    private int free;

    public PlaceSpaces() {
    }

    public PlaceSpaces(int total, int free) {
        this.total = total;
        this.free = free;
    }
}
