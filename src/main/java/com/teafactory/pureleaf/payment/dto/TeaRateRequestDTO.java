package com.teafactory.pureleaf.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeaRateRequestDTO {
    private Long userId;
    private String month;
    private BigDecimal nsa;
    private BigDecimal gsa;
    private BigDecimal monthlyRate;
    private BigDecimal totalWeight;
    private BigDecimal finalRatePerKg;
    private BigDecimal totalPayout;
}
