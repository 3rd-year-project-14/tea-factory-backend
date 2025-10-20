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
@Table(name = "payment_audit_log",
    indexes = {
        @Index(name = "idx_payment_audit_log_payment_id", columnList = "payment_id"),
        @Index(name = "idx_payment_audit_log_changed_at", columnList = "changed_at"),
        @Index(name = "idx_payment_audit_log_changed_by", columnList = "changed_by")
    }
)
public class PaymentAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", length = 20, nullable = false)
    private String paymentId;

    @Column(name = "old_status", length = 30)
    private String oldStatus;

    @Column(name = "new_status", length = 30, nullable = false)
    private String newStatus;

    @Column(name = "changed_by", length = 20, nullable = false)
    private String changedBy;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name = "action", length = 50, nullable = false)
    private String action;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_auditlog_payment"))
    private Payment payment;
}

