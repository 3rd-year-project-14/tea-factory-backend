package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.BankCsvBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BankCsvBatchRepository extends JpaRepository<BankCsvBatch, String> {
    List<BankCsvBatch> findByFactoryIdAndStatus(String factoryId, String status);
    List<BankCsvBatch> findByGeneratedAtBetween(LocalDateTime from, LocalDateTime to);
    BankCsvBatch findTopByOrderByBatchNumberDesc();
}

