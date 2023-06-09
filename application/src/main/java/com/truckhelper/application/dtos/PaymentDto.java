package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PaymentDto {
    private String method;

    private String receiptId;

    private String paidAt;
}
