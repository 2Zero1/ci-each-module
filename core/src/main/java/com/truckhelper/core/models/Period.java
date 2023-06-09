package com.truckhelper.core.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Period implements Serializable {
    private LocalDateTime beginningDate;

    private LocalDateTime endDate;

    private Period() {
    }

    public Period(LocalDateTime beginningDate, LocalDateTime endDate) {
        this.beginningDate = beginningDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Period{" +
                "beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                '}';
    }

    public static Period monthly(LocalDate beginningDate) {
        return new Period(
                beginningDate.atTime(19, 0),
                beginningDate.plusMonths(1).minusDays(1).atTime(8, 0));
    }

    public static Period yearly(LocalDate beginningDate) {
        return new Period(
                beginningDate.atTime(19, 0),
                beginningDate.plusYears(1).minusDays(1).atTime(8, 0));
    }

    public static Period fake() {
        LocalDateTime now = LocalDateTime.now();
        return new Period(now, now.plusMonths(1));
    }
}
