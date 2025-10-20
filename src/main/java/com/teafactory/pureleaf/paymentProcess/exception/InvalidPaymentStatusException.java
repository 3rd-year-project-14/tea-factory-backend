package com.teafactory.pureleaf.paymentProcess.exception;

public class InvalidPaymentStatusException extends RuntimeException {
    public InvalidPaymentStatusException(String currentStatus, String attemptedStatus) {
        super("Cannot change payment status from " + currentStatus + " to " + attemptedStatus);
    }
}

