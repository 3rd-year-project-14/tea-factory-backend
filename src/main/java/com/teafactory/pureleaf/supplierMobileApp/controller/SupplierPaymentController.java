package com.teafactory.pureleaf.supplierMobileApp.controller;

import com.teafactory.pureleaf.supplierMobileApp.dto.SupplierPaymentDTO;
import com.teafactory.pureleaf.supplierMobileApp.dto.SupplierPaymentSummaryDTO;
import com.teafactory.pureleaf.supplierMobileApp.service.SupplierPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.teafactory.pureleaf.supplierMobileApp.dto.SupplierDashboardSummaryDTO;

@RestController
@RequestMapping("/api/supplier/payments")
@RequiredArgsConstructor
public class SupplierPaymentController {
    private final SupplierPaymentService supplierPaymentService;

    @GetMapping("/history")
    public List<SupplierPaymentDTO> getSupplierPaymentHistory(
            @RequestParam String supplierId,
            @RequestParam int month,
            @RequestParam int year) {
        return supplierPaymentService.getSupplierPaymentHistory(supplierId, month, year);
    }

    @GetMapping("/dashboard-summary")
    public SupplierDashboardSummaryDTO getSupplierDashboardSummary(
            @RequestParam String supplierId,
            @RequestParam int month,
            @RequestParam int year) {
        return supplierPaymentService.getSupplierDashboardSummary(supplierId, month, year);
    }}