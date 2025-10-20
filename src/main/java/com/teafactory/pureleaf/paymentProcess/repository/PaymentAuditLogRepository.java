package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.PaymentAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentAuditLogRepository extends JpaRepository<PaymentAuditLog, Long> {
    List<PaymentAuditLog> findByPaymentIdOrderByChangedAtDesc(String paymentId);
    List<PaymentAuditLog> findByChangedAtBetween(LocalDateTime from, LocalDateTime to);
    List<PaymentAuditLog> findByChangedBy(String changedBy);
}

