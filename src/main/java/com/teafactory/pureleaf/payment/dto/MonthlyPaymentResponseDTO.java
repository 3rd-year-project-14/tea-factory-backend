package com.teafactory.pureleaf.payment.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonthlyPaymentResponseDTO {
    private Long id;
    private Long supplierId;
    private Long teaRateId;
    private BigDecimal advanceDeduction;
    private String approvedUser;
    private LocalDateTime createdAt;
    private BigDecimal finalPayment;
    private BigDecimal grossPayment;
    private BigDecimal loanDeduction;
    private String monthPeriod;
    private BigDecimal totalNetWeight;
    private BigDecimal transportDeduction;
}

