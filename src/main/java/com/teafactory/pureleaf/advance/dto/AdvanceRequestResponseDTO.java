package com.teafactory.pureleaf.advance.dto;

import com.teafactory.pureleaf.advance.entity.AdvanceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceRequestResponseDTO {
    private Long advanceId;
    private Long supplierId;
    private BigDecimal amount;
    private AdvanceRequest.PaymentMethod paymentMethod;
    private AdvanceRequest.Status status;
    private LocalDate date;
}
