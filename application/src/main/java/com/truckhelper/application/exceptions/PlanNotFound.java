package com.truckhelper.application.exceptions;

import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlanType;

public class PlanNotFound extends RuntimeException {
    public PlanNotFound(PlaceId placeId, PlanType planType) {
        super("Plan Not Found (" +
                "Place ID: " + placeId + ", " +
                "Plan: " + planType + ")");
    }
}
