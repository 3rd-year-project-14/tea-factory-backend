package com.teafactory.pureleaf.paymentProcess.exception;

public class InsufficientPaymentDataException extends RuntimeException {
    public InsufficientPaymentDataException(String message) {
        super(message);
    }
}

