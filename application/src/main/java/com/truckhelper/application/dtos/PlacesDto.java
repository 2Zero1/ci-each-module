package com.truckhelper.application.dtos;

import java.util.List;

public record PlacesDto(
        List<PlaceDto> places
) {
}
