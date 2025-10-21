package com.teafactory.pureleaf.loanProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatsDTO {
    private long completedLoanCount;
    private BigDecimal completedLoanTotal;
    private long approvedLoanCount;
    private BigDecimal approvedLoanTotal;
    private long pendingLoanRequestCount;
    private BigDecimal pendingLoanRequestTotal;
}

