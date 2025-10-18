package com.teafactory.pureleaf.advanceProcess.repository;

import com.teafactory.pureleaf.advanceProcess.dto.AdvanceStatusCountDto;
import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance;
import com.teafactory.pureleaf.advanceProcess.entity.SupplierAdvance.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierAdvanceRepository extends JpaRepository<SupplierAdvance, Long>, JpaSpecificationExecutor<SupplierAdvance> {
    List<SupplierAdvance> findBySupplier_SupplierIdAndStatusOrderByRequestedDateDesc(Long supplierId, Status status);

    List<SupplierAdvance> findByStatusOrderByRequestedDateAsc(Status status);

    List<SupplierAdvance> findBySupplier_SupplierIdOrderByRequestedDateDesc(Long supplierId);

    @Query("SELECT sa FROM SupplierAdvance sa WHERE sa.supplier.supplierId = :supplierId AND sa.status = 'APPROVED' AND sa.paidDate IS NULL")
    List<SupplierAdvance> findApprovedButUnpaidAdvances(Long supplierId);

    Optional<SupplierAdvance> findByIdAndSupplier_SupplierId(Long id, Long supplierId);

    @Query("SELECT new com.teafactory.pureleaf.advanceProcess.dto.AdvanceStatusCountDto(sa.status, COUNT(sa)) " +
           "FROM SupplierAdvance sa " +
           "WHERE sa.supplier.factory.factoryId = :factoryId " +
           "AND date_part('year', sa.requestedDate) = :year " +
           "AND date_part('month', sa.requestedDate) = :month " +
           "GROUP BY sa.status")
    List<AdvanceStatusCountDto> countAdvancesByStatus(@Param("factoryId") Long factoryId, @Param("year") int year, @Param("month") int month);
}
