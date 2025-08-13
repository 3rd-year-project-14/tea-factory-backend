package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.TripDTO;
import com.teafactory.pureleaf.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
