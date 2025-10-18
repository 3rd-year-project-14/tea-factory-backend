package com.teafactory.pureleaf.advanceProcess.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvanceResponseDto {
    private Long id;
    private String supplierName;
    private LocalDate requestedDate;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private String purpose;
    private Status status;
    private PaymentMethod paymentMethod;
    private String rejectionReason;

    public enum Status {
        REQUESTED,
        APPROVED,
        REJECTED,
        PAID
    }

    public enum PaymentMethod {
        CASH,
        BANK
    }
}
