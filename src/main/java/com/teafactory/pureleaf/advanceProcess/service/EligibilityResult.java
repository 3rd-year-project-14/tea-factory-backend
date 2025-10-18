package com.teafactory.pureleaf.advanceProcess.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record EligibilityResult(
    boolean eligible,
    BigDecimal maxAllowedAmount,
    String message,
    List<String> validationErrors
) {
    public static EligibilityResult eligible(BigDecimal maxAmount) {
        return new EligibilityResult(true, maxAmount, "Eligible for advance", new ArrayList<>());
    }

    public static EligibilityResult ineligible(String message, List<String> errors) {
        return new EligibilityResult(false, BigDecimal.ZERO, message, errors);
    }
}

