package com.teafactory.pureleaf.loanProcess.repository;

import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
}

