package com.teafactory.pureleaf.supplier.repository;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByIsActiveTrue();
    Supplier findByUser_Id(Long userId);
    long countByIsActiveIsTrueAndFactory_factoryId(Long factoryId);

    @Query("SELECT new com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO(s.supplierId, s.user.name, s.route.name, s.approvedDate) FROM Supplier s WHERE s.factory.factoryId = :factoryId")
    List<SupplierDetailsDTO> findSupplierDetailsByFactoryId(@Param("factoryId") Long factoryId);
}
