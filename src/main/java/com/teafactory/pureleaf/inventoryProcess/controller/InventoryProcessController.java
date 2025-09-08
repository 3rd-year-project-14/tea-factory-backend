package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.TareWeightRequest;
import com.teafactory.pureleaf.inventoryProcess.service.InventoryProcessService;
import com.teafactory.pureleaf.inventoryProcess.dto.TripsResponse;
import com.teafactory.pureleaf.inventoryProcess.dto.TripBagSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin (origins = "*")
@RestController
@RequestMapping("/api/inventory-process")

public class InventoryProcessController {
    @Autowired
    private InventoryProcessService inventoryProcessService;

    @GetMapping("/factory/{factoryId}")
    public ResponseEntity<?> getByFactoryId(@PathVariable Long factoryId){
        List<TripsResponse> trips = inventoryProcessService.getTodayTripsByFactory(factoryId);
        return ResponseEntity.ok(trips);
    }

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
    public ResponseEntity<List<Long>> getBagWeightIdsBySupplyRequestAndDate(@PathVariable Long supplyRequestId) {
        List<Long> bagWeightIds = inventoryProcessService.getBagWeightIdsBySupplyRequestAndDate(supplyRequestId);
        return ResponseEntity.ok(bagWeightIds);
    }

    @PutMapping("/empty-bag/{bagWeightId}")
    public ResponseEntity<?> updateTareWeight(
            @PathVariable Long bagWeightId,
            @RequestBody TareWeightRequest tareWeightRequest) {
        inventoryProcessService.updateTareWeightAndCompleteProcess(bagWeightId, tareWeightRequest.getTareWeight());
        return ResponseEntity.ok().build();
    }
}
