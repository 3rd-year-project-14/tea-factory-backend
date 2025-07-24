package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByIsActiveTrue();
    Supplier findByUser_Id(Long userId);
}
