package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class RentalDto {
    private String id;

    private String placeName;

    private String status;

    private String plan;

    private PeriodDto period;

    private Long price;

    private PaymentDto payment;
}
