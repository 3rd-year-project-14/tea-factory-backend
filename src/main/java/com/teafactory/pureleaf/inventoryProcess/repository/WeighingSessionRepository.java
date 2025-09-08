package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.WeighingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface WeighingSessionRepository extends JpaRepository<WeighingSession, Long> {
    List<WeighingSession> findByTrip_TripId(Long tripId);
    WeighingSession findFirstByTrip_TripId(Long tripId);

    // Get today's session for a trip (latest by start time)
    WeighingSession findFirstByTrip_TripIdAndSessionDateOrderByStartTimeDesc(Long tripId, LocalDate sessionDate);
}
