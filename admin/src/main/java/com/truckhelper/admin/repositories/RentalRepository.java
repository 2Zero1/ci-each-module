package com.truckhelper.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;

public interface RentalRepository extends JpaRepository<Rental, RentalId> {
    List<Rental> findRentalsByStatus(RentalStatus rentalStatus);
}
