package com.teafactory.pureleaf.paymentProcess.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum DisbursementMethod {
    BANK("Bank Transfer"),
    CASH("Cash");

    private final String displayName;

    DisbursementMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Converter(autoApply = true)
    public static class DisbursementMethodConverter implements AttributeConverter<DisbursementMethod, String> {
        @Override
        public String convertToDatabaseColumn(DisbursementMethod attribute) {
            return attribute == null ? null : attribute.name();
        }
        @Override
        public DisbursementMethod convertToEntityAttribute(String dbData) {
            return dbData == null ? null : DisbursementMethod.valueOf(dbData);
        }
    }
}

