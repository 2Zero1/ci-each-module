package com.truckhelper.core.models;

import java.time.LocalDate;

public class Plan {
    private PlanType planType;

    private Money price;

    public Plan(PlanType planType, Money price) {
        this.planType = planType;
        this.price = price;
    }

    public Money price() {
        return price;
    }

    public Money priceWithVAT() {
        return price.times(1.1);
    }

    public boolean is(PlanType planType) {
        return this.planType.equals(planType);
    }

    public Period period(LocalDate beginningDate) {
        return planType.equals(PlanType.Monthly)
                ? Period.monthly(beginningDate)
                : Period.yearly(beginningDate);
    }
}
