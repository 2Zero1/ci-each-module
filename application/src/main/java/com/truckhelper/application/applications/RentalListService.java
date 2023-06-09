package com.truckhelper.application.applications;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.truckhelper.application.exceptions.PlaceNotFound;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.RentalRepository;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.UserId;

@Service
public class RentalListService {
    private final RentalRepository rentalRepository;

    private final PlaceRepository placeRepository;

    public RentalListService(RentalRepository rentalRepository,
                             PlaceRepository placeRepository) {
        this.rentalRepository = rentalRepository;
        this.placeRepository = placeRepository;
    }

    public List<Rental> list(UserId userId) {
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);

        return rentals.stream()
                .map(this::fillPlace)
                .collect(Collectors.toList());
    }

    private Rental fillPlace(Rental rental) {
        Place place = placeRepository.findById(rental.placeId())
                .orElseThrow(() -> new PlaceNotFound(rental.placeId()));

        rental.setPlaceName(place.name());

        return rental;
    }
}
