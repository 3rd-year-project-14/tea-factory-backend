package com.teafactory.pureleaf.payment.service;

import com.teafactory.pureleaf.payment.dto.MonthlyPaymentRequestDTO;
import com.teafactory.pureleaf.payment.dto.MonthlyPaymentResponseDTO;
import com.teafactory.pureleaf.payment.entity.MonthlyPayment;
import com.teafactory.pureleaf.payment.entity.TeaRate;
import com.teafactory.pureleaf.payment.repository.MonthlyPaymentRepository;
import com.teafactory.pureleaf.payment.repository.TeaRateRepository;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonthlyPaymentService {
    @Autowired
    private MonthlyPaymentRepository monthlyPaymentRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private TeaRateRepository teaRateRepository;

    @Transactional
    public MonthlyPayment addMonthlyPayment(MonthlyPaymentRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        TeaRate teaRate = teaRateRepository.findById(dto.getTeaRateId())
                .orElseThrow(() -> new IllegalArgumentException("TeaRate not found"));

        MonthlyPayment payment = new MonthlyPayment();
        payment.setSupplier(supplier);
        payment.setTeaRate(teaRate);
        payment.setAdvanceDeduction(dto.getAdvanceDeduction());
        payment.setApprovedUser(dto.getApprovedUser());
        payment.setFinalPayment(dto.getFinalPayment());
        payment.setGrossPayment(dto.getGrossPayment());
        payment.setLoanDeduction(dto.getLoanDeduction());
        payment.setMonthPeriod(dto.getMonthPeriod());
        payment.setTotalNetWeight(dto.getTotalNetWeight());
        payment.setTransportDeduction(dto.getTransportDeduction());
        // createdAt is set by @PrePersist
        return monthlyPaymentRepository.save(payment);
    }

    public MonthlyPaymentResponseDTO toResponseDTO(MonthlyPayment payment) {
        MonthlyPaymentResponseDTO dto = new MonthlyPaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setSupplierId(payment.getSupplier() != null ? payment.getSupplier().getSupplierId() : null);
        dto.setTeaRateId(payment.getTeaRate() != null ? payment.getTeaRate().getTeaRateId() : null);
        dto.setAdvanceDeduction(payment.getAdvanceDeduction());
        dto.setApprovedUser(payment.getApprovedUser());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setFinalPayment(payment.getFinalPayment());
        dto.setGrossPayment(payment.getGrossPayment());
        dto.setLoanDeduction(payment.getLoanDeduction());
        dto.setMonthPeriod(payment.getMonthPeriod());
        dto.setTotalNetWeight(payment.getTotalNetWeight());
        dto.setTransportDeduction(payment.getTransportDeduction());
        return dto;
    }
}
