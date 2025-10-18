package com.teafactory.pureleaf.advanceProcess.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvanceRequestDto {
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "0.01", message = "Requested amount must be greater than zero")
    private BigDecimal requestedAmount;

    private String purpose;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CASH,
        BANK
    }
}
