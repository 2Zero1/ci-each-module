package com.truckhelper.admin.applications;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truckhelper.admin.exceptions.RentalNotFound;
import com.truckhelper.admin.repositories.RentalRepository;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> list(RentalStatus rentalStatus) {
        return rentalRepository.findRentalsByStatus(rentalStatus);
    }

    public void updateStatus(RentalId rentalId, RentalStatus status) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFound(rentalId));
        rental.changeStatus(status);
    }
}
