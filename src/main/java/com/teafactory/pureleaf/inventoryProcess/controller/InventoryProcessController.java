package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.entity.Trip;
import com.teafactory.pureleaf.inventoryProcess.service.InventoryProcessService;
import com.teafactory.pureleaf.inventoryProcess.dto.TripsResponse;
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
}
