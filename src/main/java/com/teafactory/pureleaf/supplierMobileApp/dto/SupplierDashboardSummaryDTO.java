package com.teafactory.pureleaf.supplierMobileApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDashboardSummaryDTO {
    private double totalNetWeight;
    private double averageTeaRate;
    private double approvedCashAdvancePayments;
    private double approvedLoanPayments;
}

