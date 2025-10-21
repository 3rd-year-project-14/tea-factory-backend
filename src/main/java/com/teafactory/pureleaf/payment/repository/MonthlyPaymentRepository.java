package com.teafactory.pureleaf.payment.repository;

import com.teafactory.pureleaf.payment.entity.MonthlyPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyPaymentRepository extends JpaRepository<MonthlyPayment, Long> {
}

