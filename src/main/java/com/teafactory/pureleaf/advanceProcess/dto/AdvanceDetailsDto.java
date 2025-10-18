package com.teafactory.pureleaf.advanceProcess.dto;

import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvanceDetailsDto {
    // Advance Details
    private Long id;
    private String supplierName;
    private LocalDate requestedDate;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private String purpose;
    private SupplierAdvance.Status status;
    private SupplierAdvance.PaymentMethod paymentMethod;
    private String rejectionReason;

    // Supplier Financials
    private BigDecimal lastMonthIncome;
    private BigDecimal thisMonthWeight;
    private BigDecimal loanAmount;
    private BigDecimal fertilizerLoan;
    private BigDecimal thisMonthIncome;

    // Eligibility
    private String eligibilityStatus; // "PASS" or "FAIL"
    private List<String> eligibilityFailReasons;
}
