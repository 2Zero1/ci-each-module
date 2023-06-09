package com.truckhelper.admin.exceptions;

import com.truckhelper.core.models.RentalId;

public class RentalNotFound extends RuntimeException {
    public RentalNotFound(RentalId rentalId) {
        super("Rental Not Found (ID: " + rentalId + ")");
    }
}
