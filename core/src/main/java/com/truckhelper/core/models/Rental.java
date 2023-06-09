package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;

@Entity
@Table(name = "rentals")
public class Rental {
    @EmbeddedId
    private RentalId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "place_id"))
    private PlaceId placeId;

    @Transient
    private String placeName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    private RentalStatus status;

    private PlanType plan;

    @Embedded
    private Period period;

    @Column(name = "price")
    private Money price;

    @Embedded
    private Payment payment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Rental() {
    }

    @Builder
    public Rental(
            RentalId id, PlaceId placeId, UserId userId, RentalStatus status,
            PlanType plan, Period period, Money price, Payment payment) {
        this.id = id;
        this.placeId = placeId;
        this.userId = userId;
        this.status = status;
        this.plan = plan;
        this.period = period;
        this.price = price;
        this.payment = payment;
    }

    public PlaceId placeId() {
        return placeId;
    }

    public RentalStatus status() {
        return status;
    }

    // TODO: Delete this method and `placeName` property
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void changeStatus(RentalStatus status) {
        this.status = status;
    }

    public static Rental fake(PlaceId placeId) {
        return Rental.builder()
                .id(new RentalId("RENTAL-ID"))
                .placeId(placeId)
                .userId(new UserId("tester"))
                .status(RentalStatus.Paid)
                .plan(PlanType.Monthly)
                .period(Period.fake())
                .price(Money.krw(250_000L))
                .payment(new Payment(PayMethod.Card))
                .build();
    }

    public static Rental fake(RentalId rentalId) {
        return Rental.builder()
                .id(rentalId)
                .placeId(new PlaceId("place-id"))
                .userId(new UserId("tester"))
                .status(RentalStatus.Paid)
                .plan(PlanType.Monthly)
                .period(Period.fake())
                .price(Money.krw(250_000L))
                .payment(new Payment(PayMethod.Card))
                .build();
    }

    public static Rental fake(RentalStatus status) {
        return Rental.builder()
                .id(new RentalId("RENTAL-ID"))
                .placeId(new PlaceId("place-id"))
                .userId(new UserId("tester"))
                .status(status)
                .plan(PlanType.Monthly)
                .period(Period.fake())
                .price(Money.krw(250_000L))
                .payment(new Payment(PayMethod.Card))
                .build();
    }
}
