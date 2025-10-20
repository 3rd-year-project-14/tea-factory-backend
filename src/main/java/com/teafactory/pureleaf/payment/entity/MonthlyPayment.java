package com.teafactory.pureleaf.payment.entity;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tea_rate_id", nullable = false)
    private TeaRate teaRate;

    @Column(name = "advance_deduction")
    private BigDecimal advanceDeduction;

    @Column(name = "approved_user")
    private String approvedUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "final_payment")
    private BigDecimal finalPayment;

    @Column(name = "gross_payment")
    private BigDecimal grossPayment;

    @Column(name = "loan_deduction")
    private BigDecimal loanDeduction;

    @Column(name = "month_period")
    private String monthPeriod;

    @Column(name = "total_net_weight")
    private BigDecimal totalNetWeight;

    @Column(name = "transport_deduction")
    private BigDecimal transportDeduction;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

