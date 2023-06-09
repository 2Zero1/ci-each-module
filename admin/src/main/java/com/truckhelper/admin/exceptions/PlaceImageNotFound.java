package com.truckhelper.admin.exceptions;

import com.truckhelper.core.models.PlaceImageId;

public class PlaceImageNotFound extends RuntimeException {
    public PlaceImageNotFound(PlaceImageId placeImageId) {
        super("PlaceImage Not Found (ID: " + placeImageId + ")");
    }
}
