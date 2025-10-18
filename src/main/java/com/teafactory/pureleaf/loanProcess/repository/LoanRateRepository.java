package com.teafactory.pureleaf.loanProcess.repository;

import com.teafactory.pureleaf.loanProcess.entity.LoanRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LoanRateRepository extends JpaRepository<LoanRate, Long> {
    Optional<LoanRate> findFirstByStatusTrueOrderByEffectiveDateDesc();
    Optional<LoanRate> findFirstByEffectiveDateLessThanEqualAndStatusTrueOrderByEffectiveDateDesc(LocalDate date);
}

