package com.teafactory.pureleaf.paymentProcess.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String paymentId) {
        super("Payment not found with ID: " + paymentId);
    }
}

