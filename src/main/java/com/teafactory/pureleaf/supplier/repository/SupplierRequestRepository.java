package com.teafactory.pureleaf.supplier.repository;

import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRequestRepository extends JpaRepository<SupplierRequest, Long> {
    List<SupplierRequest> findByStatusIn(List<String> statuses);
    List<SupplierRequest> findByUser_Id(Long userId);
    Long countByStatusAndFactory_factoryId(String status, Long factoryId);
}
