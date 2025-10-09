package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.RouteInventorySummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.repository.BagWeightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<RouteInventorySummaryDTO> getRouteInventorySummary(
            Long factoryId,
            String viewMode,
            LocalDate date,
            Integer month,
            Integer year,
            String search,
            int page,
            int size,
            String sortDir
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "totalGrossWeight"));
        // Defensive: ensure search is always a String
        if (search == null) {
            search = "";
        }
        if ("daily".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getRouteInventorySummaryByFactoryAndDate(factoryId, date, search, pageable);
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getRouteInventorySummaryByFactoryAndMonth(factoryId, month, year, search, pageable);
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }
}
