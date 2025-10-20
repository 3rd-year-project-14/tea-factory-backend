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
@Table(name = "bank_csv_payments",
    uniqueConstraints = @UniqueConstraint(name = "uk_bank_csv_payment", columnNames = {"payment_id", "batch_id"}),
    indexes = {
        @Index(name = "idx_bank_csv_payments_batch_id", columnList = "batch_id"),
        @Index(name = "idx_bank_csv_payments_payment_id", columnList = "payment_id")
    }
)
public class BankCsvPayment {
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
    @JoinColumn(name = "batch_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_csv_payment_batch"))
    private BankCsvBatch batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_csv_payment_payment"))
    private Payment payment;
}

