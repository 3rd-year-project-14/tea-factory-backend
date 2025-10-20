package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.SupplierFertilizerRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierFertilizerRequestItemRepository extends JpaRepository<SupplierFertilizerRequestItem, Long> {
}

