package com.teafactory.pureleaf.paymentProcess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cash_collection_batches",
    indexes = {
        @Index(name = "idx_cash_collection_batches_route_id", columnList = "route_id"),
        @Index(name = "idx_cash_collection_batches_driver_id", columnList = "driver_id"),
        @Index(name = "idx_cash_collection_batches_collected_at", columnList = "collected_at")
    }
)
public class CashCollectionBatch {
    @Id
    @Column(name = "id", length = 25, nullable = false)
    private String id; // CASH-YYYYMMDD-XXX

    @Column(name = "batch_number", nullable = false, unique = true)
    private Long batchNumber;

    @Column(name = "route_id", length = 20, nullable = false)
    private String routeId;

    @Column(name = "driver_id", length = 20, nullable = false)
    private String driverId;

    @Column(name = "factory_id", length = 20, nullable = false)
    private String factoryId;

    @Column(name = "total_payments", nullable = false)
    private Integer totalPayments;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(name = "collected_at", nullable = false, updatable = false)
    private LocalDateTime collectedAt;

    @Column(name = "collected_by", length = 20, nullable = false)
    private String collectedBy;

    @Column(name = "receipt_number", length = 30)
    private String receiptNumber;

    @Column(name = "receipt_printed", nullable = false)
    private Boolean receiptPrinted = false;

    @Column(name = "notes", length = 500)
    private String notes;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashCollectionPayment> payments;
}
