package com.teafactory.pureleaf.paymentProcess.exception;

public class PaymentAlreadyProcessedException extends RuntimeException {
    public PaymentAlreadyProcessedException(String paymentId) {
        super("Payment " + paymentId + " has already been processed");
    }
}

