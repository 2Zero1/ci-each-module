package com.truckhelper.application.applications;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.RentalRepository;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.UserId;


class RentalListServiceTest {
    private RentalRepository rentalRepository;

    private PlaceRepository placeRepository;

    private RentalListService rentalListService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);

        placeRepository = mock(PlaceRepository.class);

        rentalListService = new RentalListService(
                rentalRepository, placeRepository);
    }

    @Test
    void list() {
        UserId userId = new UserId("kakao-tester");
        PlaceId placeId = new PlaceId("0001PLACE");

        given(rentalRepository.findAllByUserId(userId))
                .willReturn(List.of(Rental.fake(placeId)));

        given(placeRepository.findById(placeId))
                .willReturn(Optional.of(Place.fake(placeId)));

        List<Rental> rentals = rentalListService.list(userId);

        assertThat(rentals).hasSize(1);
    }
}
