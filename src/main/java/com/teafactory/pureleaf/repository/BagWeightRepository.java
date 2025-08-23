package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.BagWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
