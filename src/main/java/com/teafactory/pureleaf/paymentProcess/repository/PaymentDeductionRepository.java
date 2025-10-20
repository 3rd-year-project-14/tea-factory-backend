package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.PaymentDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentDeductionRepository extends JpaRepository<PaymentDeduction, Long> {
    List<PaymentDeduction> findByMonthlyPaymentId(String monthlyPaymentId);
    List<PaymentDeduction> findByDeductionPaymentId(String deductionPaymentId);
    @Query("SELECT SUM(pd.amount) FROM PaymentDeduction pd WHERE pd.monthlyPaymentId = :monthlyPaymentId")
    BigDecimal sumAmountsByMonthlyPaymentId(@Param("monthlyPaymentId") String monthlyPaymentId);
}

