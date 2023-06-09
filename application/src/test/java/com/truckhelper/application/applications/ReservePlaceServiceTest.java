package com.truckhelper.application.applications;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.truckhelper.application.dtos.ReserveDto;
import com.truckhelper.application.exceptions.PlaceNotFound;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.RentalRepository;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.UserId;

class ReservePlaceServiceTest {
    private RentalRepository rentalRepository;

    private PlaceRepository placeRepository;

    private ReservePlaceService reservePlaceService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);

        placeRepository = mock(PlaceRepository.class);

        reservePlaceService = new ReservePlaceService(
                rentalRepository, placeRepository);
    }

    @Test
    @DisplayName("ReservePlaceService.reserve (successful)")
    void reservePlace() {
        UserId userId = new UserId("kakao-tester");
        PlaceId placeId = new PlaceId("0001PLACE");

        Place place = Place.fake(placeId);

        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));

        ReserveDto reserveDto = ReserveDto.fake(place);

        reservePlaceService.reserve(userId, reserveDto);

        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    @DisplayName("ReservePlaceService.reserve (when place doesn't exist)")
    void reservePlaceWhenPlaceDoesNotExist() {
        UserId userId = new UserId("kakao-tester");

        ReserveDto reserveDto = ReserveDto.fake(Place.fake("PLACE"));

        assertThrows(PlaceNotFound.class, () -> {
            reservePlaceService.reserve(userId, reserveDto);
        });

        verify(rentalRepository, never()).save(any());
    }
}
