package com.teafactory.pureleaf.inventoryProcess.service;

import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventorySummaryDto;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.InventoryRouteSummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierDailySummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierMonthlySummaryDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.inventoryDashboard.SupplierRouteSummaryDTO;
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

    public InventorySummaryDto getInventorySummary(Long factoryId, String viewMode, LocalDate date, Integer month, Integer year, Long routeId) {
        if ("daily".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getInventorySummaryByFactoryAndDate(factoryId, date, routeId);
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getInventorySummaryByFactoryAndMonth(factoryId, month, year, routeId);
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }

    public Page<InventoryRouteSummaryDTO> getRouteInventorySummary(
            Long factoryId,
            String viewMode,
            LocalDate date,
            Integer month,
            Integer year,
            String search,
            Long routeId,
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
            return bagWeightRepository.getRouteInventorySummaryByFactoryAndDate(factoryId, date, search, routeId, pageable);
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            return bagWeightRepository.getRouteInventorySummaryByFactoryAndMonth(factoryId, month, year, search, routeId, pageable);
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }

    public Page<SupplierRouteSummaryDTO> getSupplierSummaryByRoute(
            Long routeId,
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
        // Do not set a property-based Sort on the Pageable here because Spring's pageable Sort
        // will be appended to the JPQL and refer to an entity property (e.g. b.totalWeight)
        // which does not exist. The repository queries include their own ORDER BY on the
        // aggregated SUM(b.grossWeight) so use an unsorted Pageable and let the query sort.
        Pageable pageable = PageRequest.of(page, size);
        if (search == null) {
            search = "";
        }
        if ("daily".equalsIgnoreCase(viewMode)) {
            if (date == null) throw new IllegalArgumentException("date is required for daily view");
            if ("asc".equalsIgnoreCase(sortDir)) {
                return bagWeightRepository.getSupplierSummaryByRouteAndDateOrderByGrossAsc(routeId, date, search, pageable);
            } else {
                return bagWeightRepository.getSupplierSummaryByRouteAndDateOrderByGrossDesc(routeId, date, search, pageable);
            }
        } else if ("monthly".equalsIgnoreCase(viewMode)) {
            if (month == null || year == null) throw new IllegalArgumentException("month and year are required for monthly view");
            if ("asc".equalsIgnoreCase(sortDir)) {
                return bagWeightRepository.getSupplierSummaryByRouteAndMonthOrderByGrossAsc(routeId, month, year, search, pageable);
            } else {
                return bagWeightRepository.getSupplierSummaryByRouteAndMonthOrderByGrossDesc(routeId, month, year, search, pageable);
            }
        } else {
            throw new IllegalArgumentException("Invalid viewMode: " + viewMode);
        }
    }

    public com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierMonthlySummaryDTO getSupplierMonthlySummary(Long supplierId, Integer month, Integer year) {
        java.time.LocalDate now = java.time.LocalDate.now();
        int queryMonth = (month != null) ? month : now.getMonthValue();
        int queryYear = (year != null) ? year : now.getYear();
        var projection = bagWeightRepository.getSupplierMonthlySummary(supplierId, queryMonth, queryYear);
        if (projection == null) return null;
        SupplierMonthlySummaryDTO dto = new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierMonthlySummaryDTO();
        dto.setSupplierId(projection.getSupplierId());
        dto.setSupplierName(projection.getSupplierName());
        dto.setContactNumber(projection.getContactNumber());
        dto.setLastDelivery(projection.getLastDelivery());
        dto.setTotalNetWeight(projection.getTotalNetWeight());
        dto.setTotalBags(projection.getTotalBags());
        dto.setTotalDeliveryDays(projection.getTotalDeliveryDays());
        dto.setTotalGrossWeight(projection.getTotalGrossWeight());
        return dto;
    }

    public java.util.List<com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierDailySummaryDTO> getSupplierDailySummary(Long supplierId, int month, int year) {
        var projections = bagWeightRepository.getSupplierDailySummary(supplierId, month, year);
        java.util.List<com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierDailySummaryDTO> result = new java.util.ArrayList<>();
        for (var p : projections) {
            SupplierDailySummaryDTO dto = new com.teafactory.pureleaf.inventoryProcess.dto.factoryDashboard.SupplierDailySummaryDTO();
            dto.setDay(p.getDay());
            dto.setBagCount(p.getBagCount());
            dto.setGrossWeight(p.getGrossWeight());
            dto.setBagWeight(p.getBagWeight());
            dto.setWater(p.getWater());
            dto.setCoarseLeaf(p.getCoarseLeaf());
            dto.setNetWeight(p.getNetWeight());
            result.add(dto);
        }
        return result;
    }
}
