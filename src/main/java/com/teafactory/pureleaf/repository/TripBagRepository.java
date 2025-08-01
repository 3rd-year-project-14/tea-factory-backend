package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.TripBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripBagRepository extends JpaRepository<TripBag, Long> {
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestIdAndTripSupplier_Trip_TripId(Long supplyRequestId, Long tripId);
}
