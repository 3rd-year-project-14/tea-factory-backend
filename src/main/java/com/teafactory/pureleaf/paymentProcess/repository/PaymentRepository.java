package com.teafactory.pureleaf.paymentProcess.repository;

import com.teafactory.pureleaf.paymentProcess.entity.Payment;
import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentStatus;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByPaymentTypeAndPeriodMonthAndPeriodYearAndStatus(PaymentType paymentType, Integer periodMonth, Integer periodYear, PaymentStatus status);
    List<Payment> findBySupplierIdAndStatusIn(String supplierId, List<PaymentStatus> status);
    List<Payment> findByRouteIdAndDisbursementMethodAndStatusIn(String routeId, DisbursementMethod disbursementMethod, List<PaymentStatus> status);
    List<Payment> findByPaymentTypeInAndStatus(List<PaymentType> paymentTypes, PaymentStatus status);
    List<Payment> findByBatchId(String batchId);
    @Query("SELECT p FROM Payment p WHERE p.supplierId = :supplierId AND p.paymentType IN ('LOAN', 'ADVANCE', 'FERTILIZER') AND p.status = 'APPROVED' AND (p.linkedMonthlyPaymentId IS NULL OR p.isDeduction = false)")
    List<Payment> findPendingDeductionsForSupplier(@Param("supplierId") String supplierId);
    List<Payment> findByPeriodMonthAndPeriodYear(Integer periodMonth, Integer periodYear);
    List<Payment> findByStatusAndDisbursementMethod(PaymentStatus status, DisbursementMethod disbursementMethod);
    long countByStatus(PaymentStatus status);
    @Query("SELECT SUM(p.netAmount) FROM Payment p WHERE p.routeId = :routeId AND p.periodMonth = :month AND p.periodYear = :year")
    BigDecimal sumNetAmountByRouteAndPeriod(@Param("routeId") String routeId, @Param("month") Integer month, @Param("year") Integer year);
    @Query("SELECT p FROM Payment p WHERE p.id LIKE CONCAT(:prefix, '%') ORDER BY p.id DESC LIMIT 1")
    Payment findTopByIdStartingWithOrderByIdDesc(@Param("prefix") String prefix);
    List<Payment> findBySupplierId(String supplierId);
}
