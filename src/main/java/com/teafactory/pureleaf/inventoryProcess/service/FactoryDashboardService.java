package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FactoryDashboardService {
    private final BagWeightRepository bagWeightRepository;

    public InventorySummaryDto getInventorySummary(Long factoryId, String viewMode, LocalDate date, Integer month, Integer year) {
        if ("daily".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getInventorySummaryByFactoryAndDate(factoryId, date);
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getInventorySummaryByFactoryAndMonth(factoryId, month, year);
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }
}

