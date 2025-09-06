package com.teafactory.pureleaf.inventoryProcess.repository;

import com.teafactory.pureleaf.inventoryProcess.entity.WeighingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeighingSessionRepository extends JpaRepository<WeighingSession, Long> {
    List<WeighingSession> findByTrip_TripId(Long tripId);
    WeighingSession findFirstByTrip_TripId(Long tripId);
}

