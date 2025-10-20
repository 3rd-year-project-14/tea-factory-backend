package com.teafactory.pureleaf.supplierMobileApp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SupplierPaymentSummaryDTO {
    private double totalNetWeight;
    private BigDecimal teaRateAverage;
    private BigDecimal totalAdvancePayments;
    private BigDecimal totalLoanPayments;
}

