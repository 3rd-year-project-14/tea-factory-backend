package com.teafactory.pureleaf.supplier.repository;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.dto.ActiveSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier,Long>, JpaSpecificationExecutor<Supplier> {

    List<Supplier> findByIsActiveTrue();
    Supplier findByUser_Id(Long userId);
    long countByIsActiveIsTrueAndFactory_factoryId(Long factoryId);

    @Query("SELECT new com.teafactory.pureleaf.supplier.dto.ActiveSuppliersDTO(s.supplierId, s.user.name, s.route.name, s.approvedDate) FROM Supplier s WHERE s.factory.factoryId = :factoryId AND s.isActive = true")
    List<ActiveSuppliersDTO> findSupplierDetailsByFactoryId(@Param("factoryId") Long factoryId);

    @Query("SELECT new com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO(s.supplierId, s.user.name, s.user.nic, s.user.email, s.user.address, s.user.contactNo, s.landSize, s.nicImage, s.pickupLocation, s.landLocation, s.approvedDate, s.route.name, s.pickupToRouteStartDistance, s.initialBagCount) FROM Supplier s WHERE s.supplierId = :supplierId")
    SupplierDetailsDTO findSupplierDetails(@Param("supplierId") Long supplierId);

}
