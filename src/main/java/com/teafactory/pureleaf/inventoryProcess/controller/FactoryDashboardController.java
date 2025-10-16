package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventoryRouteSummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.service.FactoryDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/factory-dashboard")
@RequiredArgsConstructor
public class FactoryDashboardController {
    private final FactoryDashboardService factoryDashboardService;

    @GetMapping("/inventory/{factoryId}")
    public InventorySummaryDto getInventorySummary(
            @PathVariable Long factoryId,
            @RequestParam String viewMode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        if ("daily".equalsIgnoreCase(viewMode)) {
            if (date == null) throw new IllegalArgumentException("date is required for daily view");
            return factoryDashboardService.getInventorySummary(factoryId, viewMode, date, null, null);
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            if (month == null || year == null) throw new IllegalArgumentException("month and year are required for monthly view");
            return factoryDashboardService.getInventorySummary(factoryId, viewMode, null, month, year);
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }

    @GetMapping("/inventory/route/{factoryId}")
    public Page<InventoryRouteSummaryDTO> getRouteInventorySummary(
            @PathVariable Long factoryId,
            @RequestParam String viewMode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return factoryDashboardService.getRouteInventorySummary(factoryId, viewMode, date, month, year, search, page, size, sortDir);
    }


}
