package com.teafactory.pureleaf.loanProcess.dto;

import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestResponseDTO {
    private Long reqId;
    private Long supplierId;
    private BigDecimal amount;
    private Integer months;
    private LocalDate date;
    private String type;
    private LoanRequest.Status status;
}

