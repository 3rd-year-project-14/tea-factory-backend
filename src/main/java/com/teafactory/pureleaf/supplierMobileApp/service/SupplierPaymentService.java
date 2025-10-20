package com.teafactory.pureleaf.supplierMobileApp.service;

import com.teafactory.pureleaf.paymentProcess.entity.Payment;
import com.teafactory.pureleaf.paymentProcess.repository.PaymentRepository;
import com.teafactory.pureleaf.supplierMobileApp.dto.SupplierPaymentDTO;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import com.teafactory.pureleaf.payment.repository.TeaRateRepository;
import com.teafactory.pureleaf.supplierMobileApp.dto.SupplierPaymentSummaryDTO;
import com.teafactory.pureleaf.paymentProcess.enums.PaymentType;
import com.teafactory.pureleaf.paymentProcess.enums.DisbursementMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierPaymentService {
    private final PaymentRepository paymentRepository;
    private final BagWeightRepository bagWeightRepository;
    private final TeaRateRepository teaRateRepository;

    public List<SupplierPaymentDTO> getSupplierPaymentHistory(String supplierId, int month, int year) {
        List<Payment> payments = paymentRepository.findBySupplierIdAndPeriodMonthAndPeriodYear(supplierId, month, year);
        return payments.stream().map(payment -> {
            SupplierPaymentDTO dto = new SupplierPaymentDTO();
            dto.setPaymentId(payment.getId());
            dto.setSupplierId(payment.getSupplierId());
            dto.setPaymentType(payment.getPaymentType().name());
            dto.setAmount(payment.getNetAmount());
            dto.setPaymentDate(payment.getCreatedAt() != null ? payment.getCreatedAt().toLocalDate() : null);
            dto.setStatus(payment.getStatus().name());
            dto.setDescription(payment.getNotes());
            return dto;
        }).collect(Collectors.toList());
    }

    public SupplierPaymentSummaryDTO getSupplierPaymentSummary(String supplierId, int month, int year) {
        SupplierPaymentSummaryDTO summary = new SupplierPaymentSummaryDTO();
        // 1. Total Net Weight
        Double totalNetWeight = bagWeightRepository.sumNetWeightBySupplierIdAndMonth(supplierId, month, year);
        summary.setTotalNetWeight(totalNetWeight != null ? totalNetWeight : 0.0);

        // 2. Tea Rate Average (last 6 months)
        BigDecimal teaRateSum = BigDecimal.ZERO;
        int count = 0;
        YearMonth current = YearMonth.of(year, month);
        for (int i = 0; i < 6; i++) {
            YearMonth ym = current.minusMonths(i);
            BigDecimal rate = teaRateRepository.findCurrentRateByDate(ym.toString()); // expects 'YYYY-MM' format
            if (rate != null) {
                teaRateSum = teaRateSum.add(rate);
                count++;
            }
        }
        summary.setTeaRateAverage(count > 0 ? teaRateSum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

        // 3. Advance and Loan Payments (type 'cash')
        List<Payment> payments = paymentRepository.findBySupplierIdAndPeriodMonthAndPeriodYear(supplierId, month, year);
        BigDecimal totalAdvance = BigDecimal.ZERO;
        BigDecimal totalLoan = BigDecimal.ZERO;
        for (Payment p : payments) {
            if (p.getPaymentType() == PaymentType.ADVANCE && p.getDisbursementMethod() == DisbursementMethod.CASH) {
                totalAdvance = totalAdvance.add(p.getNetAmount());
            }
            if (p.getPaymentType() == PaymentType.LOAN && p.getDisbursementMethod() == DisbursementMethod.CASH) {
                totalLoan = totalLoan.add(p.getNetAmount());
            }
        }
        summary.setTotalAdvancePayments(totalAdvance);
        summary.setTotalLoanPayments(totalLoan);
        return summary;
    }
}
