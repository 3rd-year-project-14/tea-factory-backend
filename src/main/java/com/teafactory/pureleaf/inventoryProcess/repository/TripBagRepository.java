package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.TripBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TripBagRepository extends JpaRepository<TripBag, Long>, JpaSpecificationExecutor<TripBag> {
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestIdAndTripSupplier_Trip_TripId(Long supplyRequestId, Long tripId);
    long countByTripSupplier_Trip_TripId(Long tripId);
    List<TripBag> findByTripSupplier_Trip_TripId(Long tripId);
    List<TripBag> findByTripSupplier_Trip_TripIdAndStatus(Long tripId, String status);
    List<TripBag> findByBag_BagNumberIn(List<String> bagNumbers);
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestId(Long supplyRequestId);
    List<TripBag> findByTripSupplier_TeaSupplyRequest_RequestIdAndBag_BagNumberIn(Long supplyRequestId, List<String> bagNumbers);

    // Count total bags for a trip limited to a specific date (trip date)
    long countByTripSupplier_Trip_TripIdAndTripSupplier_Trip_TripDate(Long tripId, LocalDate tripDate);

    // Count distinct supply requests for a given trip and date
    @Query("SELECT COUNT(DISTINCT ts.teaSupplyRequest.requestId) FROM TripBag tb " +
           "JOIN tb.tripSupplier ts " +
           "WHERE ts.trip.tripId = :tripId AND ts.trip.tripDate = :tripDate")
    long countDistinctSupplyRequestsByTripIdAndDate(@Param("tripId") Long tripId, @Param("tripDate") LocalDate tripDate);

    // Sum driver weights for a given trip and date
    @Query("SELECT COALESCE(SUM(tb.driverWeight), 0) FROM TripBag tb " +
           "JOIN tb.tripSupplier ts " +
           "WHERE ts.trip.tripId = :tripId AND ts.trip.tripDate = :tripDate")
    Double sumDriverWeightByTripIdAndDate(@Param("tripId") Long tripId, @Param("tripDate") LocalDate tripDate);
}
