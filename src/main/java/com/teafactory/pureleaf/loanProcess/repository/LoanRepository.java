package com.teafactory.pureleaf.loanProcess.repository;

import com.teafactory.pureleaf.loanProcess.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findBySupplier_SupplierId(Long supplierId);
}


