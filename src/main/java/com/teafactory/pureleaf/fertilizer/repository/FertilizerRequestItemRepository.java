package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FertilizerRequestItemRepository extends JpaRepository<FertilizerRequestItem, Long> {
    List<FertilizerRequestItem> findByFertilizerRequest_Id(Long fertilizerRequestId);
}

