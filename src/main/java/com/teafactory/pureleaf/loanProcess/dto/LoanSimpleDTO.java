package com.teafactory.pureleaf.loanProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanSimpleDTO {
    private String supplierName;
    private Long loanId;
    private BigDecimal loanAmount;
    private Integer months;
    private LocalDate date;
    private BigDecimal monthlyInstalment;
}

