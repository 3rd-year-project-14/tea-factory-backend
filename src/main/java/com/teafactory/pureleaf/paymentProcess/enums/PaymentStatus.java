package com.teafactory.pureleaf.paymentProcess.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum PaymentStatus {
    CALCULATED("Calculated"),
    PENDING_APPROVAL("Pending Approval"),
    APPROVED("Approved"),
    READY_FOR_DISBURSEMENT("Ready for Disbursement"),
    PROCESSING("Processing"),
    DISBURSED("Disbursed"),
    FAILED("Failed"),
    CANCELLED("Cancelled");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Converter(autoApply = true)
    public static class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
        @Override
        public String convertToDatabaseColumn(PaymentStatus attribute) {
            return attribute == null ? null : attribute.name();
        }
        @Override
        public PaymentStatus convertToEntityAttribute(String dbData) {
            return dbData == null ? null : PaymentStatus.valueOf(dbData);
        }
    }
}

