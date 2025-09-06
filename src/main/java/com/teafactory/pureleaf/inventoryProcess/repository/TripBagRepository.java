package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.TripBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripBagRepository extends JpaRepository<TripBag, Long> {
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestIdAndTripSupplier_Trip_TripId(Long supplyRequestId, Long tripId);
    long countByTripSupplier_Trip_TripId(Long tripId);
    List<TripBag> findByTripSupplier_Trip_TripId(Long tripId);
    List<TripBag> findByTripSupplier_Trip_TripIdAndStatus(Long tripId, String status);
    List<TripBag> findByBag_BagNumberIn(List<String> bagNumbers);
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestId(Long supplyRequestId);
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestIdAndBag_BagNumberIn(Long supplyRequestId, List<String> bagNumbers);
}

