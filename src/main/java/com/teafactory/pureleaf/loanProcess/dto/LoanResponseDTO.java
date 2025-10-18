package com.teafactory.pureleaf.loanProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
    private Long loanId;
    private Long supplierId;
    private BigDecimal loanAmount;
    private LocalDate date;
    private Integer months;
    private Long rateId;
    private BigDecimal monthlyInstalment;
    private BigDecimal remainingAmount;
    private String status;
}

