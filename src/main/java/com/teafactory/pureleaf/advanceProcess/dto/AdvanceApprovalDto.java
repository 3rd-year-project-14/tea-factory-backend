package com.teafactory.pureleaf.advanceProcess.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvanceApprovalDto {
    @NotNull(message = "Manager user ID is required")
    private Long approvedByUserId;

    @DecimalMin(value = "0.01", message = "Approved amount must be greater than zero")
    private BigDecimal approvedAmount;

    @NotNull(message = "Action is required")
    private Action action;

    @Size(max = 255, message = "Rejection reason must be less than 255 characters")
    private String rejectionReason;

    public enum Action {
        APPROVE,
        REJECT
    }
}
