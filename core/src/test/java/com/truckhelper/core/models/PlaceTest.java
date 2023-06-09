package com.truckhelper.core.models;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceTest {
    @Test
    void plan() {
        Place place = Place.builder()
                .id(new PlaceId("PLACE-ID"))
                .name("PLACE-NAME")
                .address(new Address("ADDRESS"))
                .position(GeoPosition.SEOUL)
                .description(new PlaceDescription("...", "..."))
                .spaces(new PlaceSpaces(20, 7))
                .plans(List.of(
                        new Plan(PlanType.Monthly, Money.krw(500_000L)),
                        new Plan(PlanType.Yearly, Money.krw(5_500_000L))
                ))
                .build();

        assertThat(place.plan(PlanType.Monthly).get().price())
                .isEqualTo(Money.krw(500_000L));

        assertThat(place.plan(PlanType.Yearly).get().price())
                .isEqualTo(Money.krw(5_500_000L));
    }
}
