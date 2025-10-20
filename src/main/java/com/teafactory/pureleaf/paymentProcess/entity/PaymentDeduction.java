package com.teafactory.pureleaf.paymentProcess.entity;

import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_deductions",
    uniqueConstraints = @UniqueConstraint(name = "uk_monthly_deduction", columnNames = {"monthly_payment_id", "deduction_payment_id"}),
    indexes = {
        @Index(name = "idx_payment_deductions_monthly", columnList = "monthly_payment_id"),
        @Index(name = "idx_payment_deductions_deduction", columnList = "deduction_payment_id")
    }
)
public class PaymentDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_payment_id", length = 20, nullable = false)
    private String monthlyPaymentId;

    @Column(name = "deduction_payment_id", length = 20, nullable = false)
    private String deductionPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "deduction_type", length = 20, nullable = false)
    private PaymentType deductionType;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_payment_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_deduction_monthly_payment"))
    private Payment monthlyPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deduction_payment_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_deduction_payment"))
    private Payment deductionPayment;
}

