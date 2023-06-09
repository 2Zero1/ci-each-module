package com.truckhelper.application.exceptions;

import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.Payment;
import com.truckhelper.core.models.Plan;

public class IncorrectPaymentPrice extends RuntimeException {
    private Payment payment;

    public IncorrectPaymentPrice(
            Plan plan, Money paymentPrice, Payment payment) {
        super("Incorrect payment price" +
                " [PlanPrice:" + plan.priceWithVAT()
                + ", PaymentPrice:" + paymentPrice
                + ", Payment:" + payment
                + "]");

        this.payment = payment;
    }
}
