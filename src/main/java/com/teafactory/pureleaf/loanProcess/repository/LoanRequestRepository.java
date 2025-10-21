package com.teafactory.pureleaf.loanProcess.repository;

import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    List<LoanRequest> findBySupplier_SupplierId(Long supplierId);

    @Query("SELECT COUNT(lr) FROM LoanRequest lr WHERE lr.status = :status AND lr.supplier.factory.factoryId = :factoryId AND MONTH(lr.date) = :month AND YEAR(lr.date) = :year")
    long countByStatusAndFactoryAndMonthAndYear(@Param("status") LoanRequest.Status status, @Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(lr.amount), 0) FROM LoanRequest lr WHERE lr.status = :status AND lr.supplier.factory.factoryId = :factoryId AND MONTH(lr.date) = :month AND YEAR(lr.date) = :year")
    BigDecimal sumByStatusAndFactoryAndMonthAndYear(@Param("status") LoanRequest.Status status, @Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);
}
