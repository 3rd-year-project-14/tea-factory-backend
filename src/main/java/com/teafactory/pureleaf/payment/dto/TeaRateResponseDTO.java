package com.teafactory.pureleaf.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeaRateResponseDTO {
    private String month;
    private BigDecimal nsa;
    private BigDecimal gsa;
    private BigDecimal monthlyRate;
    private BigDecimal totalWeight;
    private BigDecimal finalRatePerKg;
    private BigDecimal totalPayout;
    private String createdAt;
    private Long factoryId;
    private String factoryName;
    private String userName;
    private String status;
    private BigDecimal adjustedRate;
    private String adjustmentReason;
}
