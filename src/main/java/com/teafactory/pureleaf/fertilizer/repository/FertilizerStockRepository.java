package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FertilizerStockRepository extends JpaRepository<FertilizerStock, Long> {
    List<FertilizerStock> findByUserId(Long userId);
}
