package com.teafactory.pureleaf.loanProcess.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanRateRequest {
    private BigDecimal rate;
    private LocalDate effectiveDate;
    private boolean status = true;
}

