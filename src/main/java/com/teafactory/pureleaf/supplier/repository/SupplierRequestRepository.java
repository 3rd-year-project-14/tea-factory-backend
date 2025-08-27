package com.teafactory.pureleaf.supplier.repository;

import com.teafactory.pureleaf.supplier.dto.RequestSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierRequestDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRequestRepository extends JpaRepository<SupplierRequest, Long> {
    List<SupplierRequest> findByStatusIn(List<String> statuses);
    List<SupplierRequest> findByUser_Id(Long userId);
    Long countByStatusAndFactory_factoryId(String status, Long factoryId);

    @Query("SELECT new com.teafactory.pureleaf.supplier.dto.RequestSuppliersDTO(r.id, r.user.name, r.monthlySupply, r.requestedDate, r.rejectedDate) FROM SupplierRequest r WHERE r.factory.factoryId = :factoryId AND r.status = :status")
    List<RequestSuppliersDTO> findRequestsByFactoryIdAndStatus(Long factoryId, String status);

    @Query("SELECT new com.teafactory.pureleaf.supplier.dto.SupplierRequestDetailsDTO(r.id, r.user.name, r.user.nic, r.user.email, r.user.address, r.user.contactNo, r.monthlySupply, r.landSize, r.nicImage, r.pickupLocation, r.landLocation, r.requestedDate, r.rejectedDate, r.rejectReason, r.status) FROM SupplierRequest r WHERE r.id = :requestId")
    SupplierRequestDetailsDTO findRequestDetailsById(Long requestId);
}
