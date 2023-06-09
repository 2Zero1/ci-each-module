package com.truckhelper.application.applications;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.truckhelper.application.dtos.ReserveDto;
import com.truckhelper.application.exceptions.IncorrectPaymentPrice;
import com.truckhelper.application.exceptions.PlaceNotFound;
import com.truckhelper.application.exceptions.PlanNotFound;
import com.truckhelper.application.repositories.PlaceRepository;
import com.truckhelper.application.repositories.RentalRepository;
import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.PayMethod;
import com.truckhelper.core.models.Payment;
import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.Plan;
import com.truckhelper.core.models.PlanType;
import com.truckhelper.core.models.Rental;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.RentalStatus;
import com.truckhelper.core.models.UserId;

@Service
public class ReservePlaceService {
    private final RentalRepository rentalRepository;

    private final PlaceRepository placeRepository;

    public ReservePlaceService(RentalRepository rentalRepository,
                               PlaceRepository placeRepository) {
        this.rentalRepository = rentalRepository;
        this.placeRepository = placeRepository;
    }

    public void reserve(UserId userId, ReserveDto reserveDto) {
        PlaceId placeId = new PlaceId(reserveDto.getPlaceId());

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFound(placeId));

        PlanType planType = PlanType.of(reserveDto.getPlan());

        Plan plan = place.plan(planType)
                .orElseThrow(() -> new PlanNotFound(placeId, planType));

        LocalDate beginningDate =
                LocalDate.parse(reserveDto.getBeginningDate());

        Payment payment = new Payment(
                PayMethod.of(reserveDto.getMethod()),
                reserveDto.getReceiptId(),
                LocalDateTime.now()
        );

        Money paymentPrice = Money.krw(reserveDto.getPrice());
        if (!paymentPrice.equals(plan.priceWithVAT())) {
            throw new IncorrectPaymentPrice(plan, paymentPrice, payment);
        }

        Rental rental = Rental.builder()
                .id(RentalId.generate())
                .userId(userId)
                .placeId(placeId)
                .status(RentalStatus.Paid)
                .plan(PlanType.Monthly)
                .period(plan.period(beginningDate))
                .price(plan.price())
                .payment(payment)
                .build();

        rentalRepository.save(rental);
    }
}
