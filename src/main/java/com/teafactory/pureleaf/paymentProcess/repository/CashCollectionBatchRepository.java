package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.CashCollectionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashCollectionBatchRepository extends JpaRepository<CashCollectionBatch, String> {
    List<CashCollectionBatch> findByRouteIdAndCollectedAtBetween(String routeId, LocalDateTime from, LocalDateTime to);
    List<CashCollectionBatch> findByDriverId(String driverId);
    CashCollectionBatch findTopByOrderByBatchNumberDesc();
}

