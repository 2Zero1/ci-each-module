package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;


@Embeddable
public class Payment {
    private PayMethod method;

    private String receiptId;

    private LocalDateTime paidAt;

    private Payment() {
    }

    public Payment(PayMethod method) {
        this.method = method;
    }

    public Payment(PayMethod method, String receiptId, LocalDateTime paidAt) {
        this.method = method;
        this.receiptId = receiptId;
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "method=" + method +
                ", receiptId=" + receiptId +
                ", paidAt=" + paidAt +
                '}';
    }
}
