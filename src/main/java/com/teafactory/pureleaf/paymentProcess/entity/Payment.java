package com.teafactory.pureleaf.paymentProcess.entity;

import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentStatus;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments",
    indexes = {
        @Index(name = "idx_payments_supplier_id", columnList = "supplier_id"),
        @Index(name = "idx_payments_route_id", columnList = "route_id"),
        @Index(name = "idx_payments_status", columnList = "status"),
        @Index(name = "idx_payments_payment_type", columnList = "payment_type"),
        @Index(name = "idx_payments_period", columnList = "period_month,period_year"),
        @Index(name = "idx_payments_batch_id", columnList = "batch_id")
    }
)
public class Payment {
    @Id
    @Column(name = "id", length = 20, nullable = false)
    private String id; // PAY-YYYYMM-XXXX

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 20, nullable = false)
    private PaymentType paymentType;

    @Column(name = "supplier_id", length = 20, nullable = false)
    private String supplierId;

    @Column(name = "route_id", length = 20, nullable = false)
    private String routeId;

    @Column(name = "period_month")
    private Integer periodMonth;

    @Column(name = "period_year")
    private Integer periodYear;

    @Column(name = "gross_amount", precision = 15, scale = 2, nullable = false)
    @ColumnDefault("0")
    private BigDecimal grossAmount = BigDecimal.ZERO;

    @Column(name = "total_weight", precision = 15, scale = 2, nullable = false)
    @ColumnDefault("0")
    private BigDecimal totalWeight = BigDecimal.ZERO;

    @Column(name = "tea_rate", precision = 10, scale = 2, nullable = false)
    @ColumnDefault("0")
    private BigDecimal teaRate = BigDecimal.ZERO;

    @Column(name = "deduction_amount", precision = 15, scale = 2, nullable = false)
    @ColumnDefault("0")
    private BigDecimal deductionAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "disbursement_method", length = 10, nullable = false)
    private DisbursementMethod disbursementMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private PaymentStatus status;

    @Column(name = "linked_monthly_payment_id", length = 20)
    private String linkedMonthlyPaymentId;

    @Column(name = "is_deduction", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeduction = false;

    @Column(name = "batch_id", length = 30)
    private String batchId;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by", length = 20)
    private String approvedBy;

    @Column(name = "disbursed_at")
    private LocalDateTime disbursedAt;

    @Column(name = "disbursed_by", length = 20)
    private String disbursedBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

