package com.teafactory.pureleaf.loanProcess.repository;

import com.teafactory.pureleaf.loanProcess.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findBySupplier_SupplierId(Long supplierId);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = :status AND l.supplier.factory.factoryId = :factoryId AND MONTH(l.date) = :month AND YEAR(l.date) = :year")
    long countByStatusAndFactoryAndMonthAndYear(@Param("status") Loan.Status status, @Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(l.loanAmount), 0) FROM Loan l WHERE l.status = :status AND l.supplier.factory.factoryId = :factoryId AND MONTH(l.date) = :month AND YEAR(l.date) = :year")
    BigDecimal sumByStatusAndFactoryAndMonthAndYear(@Param("status") Loan.Status status, @Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT l FROM Loan l WHERE l.status = :status AND l.supplier.factory.factoryId = :factoryId AND MONTH(l.date) = :month AND YEAR(l.date) = :year")
    List<Loan> findByStatusAndFactoryAndMonthAndYear(@Param("status") Loan.Status status, @Param("factoryId") Long factoryId, @Param("month") int month, @Param("year") int year);
}
