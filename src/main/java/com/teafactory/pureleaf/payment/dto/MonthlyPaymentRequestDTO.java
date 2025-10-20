package com.teafactory.pureleaf.payment.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyPaymentRequestDTO {
    private Long supplierId;
    private Long teaRateId;
    private BigDecimal advanceDeduction;
    private String approvedUser;
    private BigDecimal finalPayment;
    private BigDecimal grossPayment;
    private BigDecimal loanDeduction;
    private String monthPeriod;
    private BigDecimal totalNetWeight;
    private BigDecimal transportDeduction;
}

