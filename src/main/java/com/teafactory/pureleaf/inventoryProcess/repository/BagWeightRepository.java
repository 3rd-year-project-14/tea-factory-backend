package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.BagWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BagWeightRepository extends JpaRepository<BagWeight, Long> {
    List<BagWeight> findBySupplyRequest_RequestIdAndDate(Long supplyRequestId, LocalDate date);

    // Sum grossWeight for a given sessionId
    @org.springframework.data.jpa.repository.Query("SELECT SUM(b.grossWeight) FROM BagWeight b WHERE b.weighingSession.sessionId = :sessionId")
    Double sumGrossWeightBySessionId(Long sessionId);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(b.tareWeight) FROM BagWeight b WHERE b.weighingSession.sessionId = :sessionId")
    Double sumTareWeightBySessionId(Long sessionId);

    List<BagWeight> findByWeighingSession_SessionId(Long sessionId);

    // Projection for trip gross weight aggregation
    interface TripGrossWeightProjection {
        Long getTripId();
        Double getTotalGrossWeight();
    }

    // Sum grossWeight grouped by trip ids (across all sessions of each trip)
    @Query("SELECT b.weighingSession.trip.tripId AS tripId, SUM(b.grossWeight) AS totalGrossWeight " +
            "FROM BagWeight b WHERE b.weighingSession.trip.tripId IN :tripIds GROUP BY b.weighingSession.trip.tripId")
    List<TripGrossWeightProjection> sumGrossWeightByTripIds(@Param("tripIds") List<Long> tripIds);

    // Projection for trip tare weight aggregation
    interface TripTareWeightProjection {
        Long getTripId();
        Double getTotalTareWeight();
    }

    // Sum tareWeight grouped by trip ids (across all sessions of each trip)
    @Query("SELECT b.weighingSession.trip.tripId AS tripId, SUM(b.tareWeight) AS totalTareWeight " +
            "FROM BagWeight b WHERE b.weighingSession.trip.tripId IN :tripIds GROUP BY b.weighingSession.trip.tripId")
    List<TripTareWeightProjection> sumTareWeightByTripIds(@Param("tripIds") List<Long> tripIds);
}
