package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.SupplierFertilizerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierFertilizerRequestRepository extends JpaRepository<SupplierFertilizerRequest, Long> {
    List<SupplierFertilizerRequest> findBySupplier_Id(Long supplierId);
}
