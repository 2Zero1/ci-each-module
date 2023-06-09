package com.truckhelper.core.models;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Money {
    public static final Money ZERO = new Money(0L);

    private Long amount;

    private Money() {
    }

    public Money(Long amount) {
        this.amount = amount;
    }

    public Money(double amount) {
        this.amount = (long) amount;
    }

    public Money plus(Money other) {
        return new Money(amount + other.amount);
    }

    public Money times(int multiplier) {
        return new Money(amount * multiplier);
    }

    public Money times(double multiplier) {
        return new Money(amount * multiplier);
    }

    public Long asLong() {
        return amount;
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    public static Money krw(Long amount) {
        return new Money(amount);
    }
}
