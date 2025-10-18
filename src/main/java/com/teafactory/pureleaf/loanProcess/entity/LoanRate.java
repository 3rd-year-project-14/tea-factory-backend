package com.teafactory.pureleaf.loanProcess.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "status", nullable = false)
    private boolean status = true;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
