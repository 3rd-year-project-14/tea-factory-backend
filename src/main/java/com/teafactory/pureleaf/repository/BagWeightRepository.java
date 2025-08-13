package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.BagWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BagWeightRepository extends JpaRepository<BagWeight, Long> {
    List<BagWeight> findBySupplyRequest_RequestIdAndDate(Long supplyRequestId, LocalDate date);
}
