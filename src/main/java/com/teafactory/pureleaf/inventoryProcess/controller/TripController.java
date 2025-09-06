package com.teafactory.pureleaf.inventoryProcess.controller;

import com.teafactory.pureleaf.inventoryProcess.dto.TripDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.TripStatusCountsDTO;
import com.teafactory.pureleaf.inventoryProcess.dto.TripSummaryResponseDTO;
import com.teafactory.pureleaf.inventoryProcess.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trips")
public class TripController {
    @Autowired
    private TripService tripService;

    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
        return tripService.getTripById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TripDTO> createTrip(@RequestBody TripDTO tripDTO) {
        TripDTO createdTrip = tripService.createTrip(tripDTO.getDriverId(), tripDTO.getRouteId());
        return ResponseEntity.ok(createdTrip);
    }

    @GetMapping("/today/{driverId}")
    public ResponseEntity<TripDTO> getTodayTripByDriverId(@PathVariable Long driverId) {
        return tripService.getTodayTripByDriverId(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TripDTO> updateTripStatus(@PathVariable Long id, @RequestBody TripDTO tripDTO) {
        return tripService.updateTripStatus(id, tripDTO.getStatus())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(null));
    }

    @GetMapping("/status-counts/factory/{factoryId}/today")
    public ResponseEntity<TripStatusCountsDTO> getTodayStatusCounts(@PathVariable Long factoryId) {
        TripStatusCountsDTO counts = tripService.getTodayTripStatusCounts(factoryId);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/factory/{factoryId}/status/{status}/today")
    public ResponseEntity<Page<TripSummaryResponseDTO>> getTodayTripSummariesByStatus(@PathVariable Long factoryId,
                                                                                      @PathVariable String status,
                                                                                      @RequestParam(required = false) String search,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size,
                                                                                      @RequestParam(defaultValue = "routeName,asc") String[] sort) {
        String sortBy = sort[0];
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TripSummaryResponseDTO> result = tripService.getTodayTripSummaries(factoryId, status, search, pageable);
        return ResponseEntity.ok(result);
    }
}
