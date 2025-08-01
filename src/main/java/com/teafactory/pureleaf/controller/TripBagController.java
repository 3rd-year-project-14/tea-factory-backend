package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.TripBagDTO;
import com.teafactory.pureleaf.dto.SupplierRequestBagSummaryDTO;
import com.teafactory.pureleaf.service.TripBagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-bags")
@CrossOrigin(origins = "*")
public class TripBagController {
    @Autowired
    private TripBagService tripBagService;

    @GetMapping
    public ResponseEntity<List<TripBagDTO>> getAllTripBags() {
        return ResponseEntity.ok(tripBagService.getAllTripBags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripBagDTO> getTripBag(@PathVariable Long id) {
        TripBagDTO tripBag = tripBagService.getTripBag(id);
        if (tripBag != null) {
            return ResponseEntity.ok(tripBag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TripBagDTO> createTripBag(@RequestBody TripBagDTO tripBagDTO) {
        TripBagDTO created = tripBagService.createTripBag(tripBagDTO);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TripBagDTO>> createTripBags(@RequestBody List<TripBagDTO> tripBagDTOs) {
        List<TripBagDTO> created = tripBagService.createTripBags(tripBagDTOs);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/by-supply-request/{supplyRequestId}/trip/{tripId}")
    public ResponseEntity<List<TripBagDTO>> getTripBagsBySupplyRequestAndTrip(@PathVariable Long supplyRequestId, @PathVariable Long tripId) {
        List<TripBagDTO> tripBags = tripBagService.getTripBagsBySupplyRequestAndTrip(supplyRequestId, tripId);
        return ResponseEntity.ok(tripBags);
    }

    @GetMapping("/summary/by-trip/{tripId}")
    public ResponseEntity<List<SupplierRequestBagSummaryDTO>> getSupplierRequestBagSummaryByTripId(@PathVariable Long tripId) {
        List<SupplierRequestBagSummaryDTO> summary = tripBagService.getSupplierRequestBagSummaryByTripId(tripId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/by-supply-request/{supplyRequestId}")
    public ResponseEntity<SupplierRequestBagSummaryDTO> getSupplierRequestBagSummaryBySupplyRequestId(@PathVariable Long supplyRequestId) {
        SupplierRequestBagSummaryDTO summary = tripBagService.getSupplierRequestBagSummaryBySupplyRequestId(supplyRequestId);
        return ResponseEntity.ok(summary);
    }
}
