package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.CashCollectionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashCollectionPaymentRepository extends JpaRepository<CashCollectionPayment, Long> {
    List<CashCollectionPayment> findByBatchId(String batchId);
    List<CashCollectionPayment> findByPaymentId(String paymentId);
    boolean existsByPaymentId(String paymentId);
}

