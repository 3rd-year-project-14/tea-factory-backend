package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.BankCsvPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCsvPaymentRepository extends JpaRepository<BankCsvPayment, Long> {
    List<BankCsvPayment> findByBatchId(String batchId);
    List<BankCsvPayment> findByPaymentId(String paymentId);
    boolean existsByPaymentId(String paymentId);
}

