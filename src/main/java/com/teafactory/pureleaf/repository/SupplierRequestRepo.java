package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.SupplierRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRequestRepo extends JpaRepository<SupplierRequest, Long> {
    List<SupplierRequest> findByStatusIn(List<String> statuses);
    List<SupplierRequest> findByUser_Id(Long userId);
}
