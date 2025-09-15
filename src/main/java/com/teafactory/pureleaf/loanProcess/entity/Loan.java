package com.teafactory.pureleaf.loanProcess.entity;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "loan_amount", nullable = false)
    @Min(0)
    private BigDecimal loanAmount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "months", nullable = false)
    private Integer months;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id", nullable = false)
    private LoanRate loanRate;

    @Column(name = "monthly_instalment", nullable = false)
    @Min(0)
    private BigDecimal monthlyInstalment;

    @Column(name = "remaining_amount", nullable = false)
    @Min(0)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.REMAINING;

    public enum Status {
        REMAINING,
        COMPLETE
    }

    @PrePersist
    @PreUpdate
    private void updateStatus() {
        if (remainingAmount != null && remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            status = Status.COMPLETE;
        } else {
            status = Status.REMAINING;
        }
    }
}
