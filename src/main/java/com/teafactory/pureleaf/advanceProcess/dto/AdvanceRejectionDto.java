package com.teafactory.pureleaf.advanceProcess.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceRejectionDto {
    @NotNull(message = "Manager user ID is required")
    private Long rejectedByUserId;

    @NotEmpty(message = "Rejection reason is required")
    private String rejectionReason;
}

