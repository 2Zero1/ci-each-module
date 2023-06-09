package com.truckhelper.core.models;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanTest {
    @Test
    void is() {
        Money price = Money.krw(100_000L);

        Plan monthly = new Plan(PlanType.Monthly, price);
        Plan yearly = new Plan(PlanType.Yearly, price);

        assertThat(monthly.is(PlanType.Monthly)).isTrue();

        assertThat(yearly.is(PlanType.Yearly)).isTrue();
    }

    @Test
    @DisplayName("period (type=Monthly)")
    void periodMonthly() {
        Plan plan = new Plan(PlanType.Monthly, Money.krw(100_000L));

        LocalDateTime beginningDateTime =
                LocalDateTime.parse("2023-02-01T19:00:00");
        LocalDateTime endDateTime =
                LocalDateTime.parse("2023-02-28T08:00:00");

        assertThat(plan.period(beginningDateTime.toLocalDate()))
                .isEqualTo(new Period(beginningDateTime, endDateTime));
    }

    @Test
    @DisplayName("period (type=Yearly)")
    void periodYearly() {
        Plan plan = new Plan(PlanType.Yearly, Money.krw(100_000L));

        LocalDateTime beginningDateTime =
                LocalDateTime.parse("2022-10-10T19:00:00");
        LocalDateTime endDateTime =
                LocalDateTime.parse("2023-10-09T08:00:00");

        assertThat(plan.period(beginningDateTime.toLocalDate()))
                .isEqualTo(new Period(beginningDateTime, endDateTime));
    }
}
