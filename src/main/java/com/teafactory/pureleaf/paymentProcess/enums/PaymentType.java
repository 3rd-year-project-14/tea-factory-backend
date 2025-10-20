package com.teafactory.pureleaf.paymentProcess.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum PaymentType {
    MONTHLY("Monthly Payment"),
    LOAN("Loan Deduction"),
    ADVANCE("Advance Deduction"),
    FERTILIZER("Fertilizer Deduction");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Converter(autoApply = true)
    public static class PaymentTypeConverter implements AttributeConverter<PaymentType, String> {
        @Override
        public String convertToDatabaseColumn(PaymentType attribute) {
            return attribute == null ? null : attribute.name();
        }
        @Override
        public PaymentType convertToEntityAttribute(String dbData) {
            return dbData == null ? null : PaymentType.valueOf(dbData);
        }
    }
}

