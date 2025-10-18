package com.teafactory.pureleaf.advanceProcess.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collections;

@Service
public class EligibilityService {
    // Stub implementation for eligibility check
    public EligibilityResult checkEligibility(Long supplierId, BigDecimal requestedAmount) {
        // Example stub logic: always eligible up to 100,000
        BigDecimal maxAllowed = new BigDecimal("100000");
        if (requestedAmount.compareTo(maxAllowed) <= 0) {
            return EligibilityResult.eligible(maxAllowed);
        } else {
            return EligibilityResult.ineligible(
                "Requested amount exceeds allowed limit",
                Collections.singletonList("Maximum allowed is " + maxAllowed)
            );
        }
    }
}

