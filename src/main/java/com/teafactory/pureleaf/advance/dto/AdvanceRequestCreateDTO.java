package com.teafactory.pureleaf.advance.dto;

import com.teafactory.pureleaf.advance.entity.AdvanceRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdvanceRequestCreateDTO {
    private Long supplierId;
    private BigDecimal amount;
    private AdvanceRequest.PaymentMethod paymentMethod;
}
