package com.teafactory.pureleaf.loanProcess.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestCreateDTO {
    private Long supplierId;
    private BigDecimal amount;
    private Integer months;
}

