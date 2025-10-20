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
@Table(name = "bank_csv_batches",
    indexes = {
        @Index(name = "idx_bank_csv_batches_factory_id", columnList = "factory_id"),
        @Index(name = "idx_bank_csv_batches_status", columnList = "status"),
        @Index(name = "idx_bank_csv_batches_generated_at", columnList = "generated_at")
    }
)
public class BankCsvBatch {
    @Id
    @Column(name = "id", length = 25, nullable = false)
    private String id; // CSV-YYYYMMDD-XXX

    @Column(name = "batch_number", nullable = false, unique = true)
    private Long batchNumber;

    @Column(name = "factory_id", length = 20, nullable = false)
    private String factoryId;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;

    @Column(name = "total_payments", nullable = false)
    private Integer totalPayments;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Column(name = "generated_by", length = 20, nullable = false)
    private String generatedBy;

    @Column(name = "sent_to_bank_at")
    private LocalDateTime sentToBankAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "notes", length = 500)
    private String notes;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankCsvPayment> payments;
}
