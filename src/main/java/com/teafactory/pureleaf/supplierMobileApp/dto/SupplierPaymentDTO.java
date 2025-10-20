package com.teafactory.pureleaf.supplierMobileApp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class SupplierPaymentDTO {
    private String paymentId;
    private String supplierId;
    private String paymentType; // e.g., ADVANCE, LOAN, MONTHLY, ADHOC
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String status;
    private String description;



}
