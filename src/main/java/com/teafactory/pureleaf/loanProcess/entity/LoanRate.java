package com.teafactory.pureleaf.loanProcess.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "loan_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @Column(name = "rate", nullable = false)
    @Min(0)
    private float rate;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
