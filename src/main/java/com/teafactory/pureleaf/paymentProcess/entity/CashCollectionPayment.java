package com.teafactory.pureleaf.paymentProcess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cash_collection_payments",
    uniqueConstraints = @UniqueConstraint(name = "uk_cash_collection_payment", columnNames = {"payment_id", "batch_id"}),
    indexes = {
        @Index(name = "idx_cash_collection_payments_batch_id", columnList = "batch_id"),
        @Index(name = "idx_cash_collection_payments_payment_id", columnList = "payment_id")
    }
)
public class CashCollectionPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", length = 25, nullable = false)
    private String batchId;

    @Column(name = "payment_id", length = 20, nullable = false)
    private String paymentId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_cash_payment_batch"))
    private CashCollectionBatch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_cash_payment_payment"))
    private Payment payment;
}

