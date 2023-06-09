package com.truckhelper.admin.applications;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.truckhelper.admin.repositories.RentalRepository;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;

class RentalServiceTest {
    RentalService rentalService;

    RentalRepository rentalRepository;

    @BeforeEach
    void setup() {
        rentalRepository = mock(RentalRepository.class);

        rentalService = new RentalService(rentalRepository);
    }

    @Test
    void list() {
        RentalStatus status = RentalStatus.New;
        given(rentalRepository.findRentalsByStatus(status))
                .willReturn(List.of(Rental.fake(status)));

        assertThat(rentalService.list(status)).hasSize(1);
    }

    @Test
    void updateStatus() {
        RentalId id = new RentalId("0001-RENTAL-ID");

        Rental rental = Rental.fake(id);

        given(rentalRepository.findById(id))
                .willReturn(Optional.of(rental));

        rentalService.updateStatus(id, RentalStatus.Accepted);

        assertThat(rental.status()).isEqualTo(RentalStatus.Accepted);
    }
}
