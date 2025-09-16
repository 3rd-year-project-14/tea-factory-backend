package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.*;
import com.teafactory.pureleaf.inventoryProcess.service.InventoryProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@CrossOrigin (origins = "*")
@RestController
@RequestMapping("/api/inventory-process")

public class InventoryProcessController {
    @Autowired
    private InventoryProcessService inventoryProcessService;

    @GetMapping("/trip/{tripId}/bags")
    public ResponseEntity<?> getBagsByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(inventoryProcessService.getBagsByTripId(tripId));
    }

    @GetMapping("/trip/{tripId}/summary")
    public ResponseEntity<TripBagSummaryResponse> getTripBagSummary(@PathVariable Long tripId) {
        return ResponseEntity.ok(inventoryProcessService.getTodayTripBagSummary(tripId));
    }

    @GetMapping("/trip/{tripId}/bags/pending")
    public ResponseEntity<?> getPendingBagsByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(inventoryProcessService.getPendingBagsByTripId(tripId));
    }

    @GetMapping("/trip/{tripId}/bags/weighed")
    public ResponseEntity<?> getWeighedBagsByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(inventoryProcessService.getWeighedBagsByTripId(tripId));
    }

    @GetMapping("/supply-request/{supplyRequestId}/bagweight-id")
    public ResponseEntity<Long> getBagWeightIdBySupplyRequestAndDate(@PathVariable Long supplyRequestId) {
        Long bagWeightId = inventoryProcessService.getBagWeightIdBySupplyRequestAndDate(supplyRequestId);
        return ResponseEntity.ok(bagWeightId);
    }

    @PutMapping("/empty-bag/{bagWeightId}")
    public ResponseEntity<?> updateTareWeight(
            @PathVariable Long bagWeightId,
            @RequestBody TareWeightRequest tareWeightRequest) {
        inventoryProcessService.updateTareWeightAndCompleteProcess(bagWeightId, tareWeightRequest.getTareWeight());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trip/{tripId}/weighing-summary")
    public ResponseEntity<WeighingSummaryResponse> getWeighingSummary(
            @PathVariable Long tripId,
            @RequestParam String status) {
        WeighingSummaryResponse resp = inventoryProcessService.getTodayWeighingSummaryByTripAndStatus(tripId, status);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{factoryId}/bagweights")
    public ResponseEntity<Page<BagWeightDetailsResponse>> getBagWeights(
            @PathVariable Long factoryId,
            @RequestParam(required = false) Long routeId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BagWeightDetailsResponse> result = inventoryProcessService.getBagWeights(
                factoryId, routeId, userId, date, search, page, size
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{factoryId}/dashboard-summary")
    public ResponseEntity<FactoryDashboardSummaryResponse> getFactoryDashboardSummary(@PathVariable Long factoryId) {
        FactoryDashboardSummaryResponse summary = inventoryProcessService.getFactoryDashboardSummary(factoryId);
        return ResponseEntity.ok(summary);
    }
}