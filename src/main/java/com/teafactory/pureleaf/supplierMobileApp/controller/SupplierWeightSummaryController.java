package com.teafactory.pureleaf.supplierMobileApp.controller;

import com.teafactory.pureleaf.supplierMobileApp.dto.WeightSummaryDTO;
import com.teafactory.pureleaf.supplierMobileApp.service.SupplierWeightSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/supplierMobileApp/weights")
public class SupplierWeightSummaryController {
    private final SupplierWeightSummaryService summaryService;

    @Autowired
    public SupplierWeightSummaryController(SupplierWeightSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/summary")
    public WeightSummaryDTO getWeightSummary(
            @RequestParam Long supplierId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return summaryService.getWeightSummary(supplierId, month, year);
    }
}

