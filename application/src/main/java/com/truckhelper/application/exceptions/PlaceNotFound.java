package com.truckhelper.application.exceptions;

import com.truckhelper.core.models.PlaceId;

public class PlaceNotFound extends RuntimeException {
    public PlaceNotFound(PlaceId placeId) {
        super("Place Not Found (ID: " + placeId + ")");
    }
}
