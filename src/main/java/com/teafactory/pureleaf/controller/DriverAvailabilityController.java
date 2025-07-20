package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.DriverAvailabilityDTO;
import com.teafactory.pureleaf.service.DriverAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver-availability")
public class DriverAvailabilityController {
    @Autowired
    private DriverAvailabilityService driverAvailabilityService;

    @GetMapping("/{driverId}")
    public ResponseEntity<List<DriverAvailabilityDTO>> getAvailabilityByDriverId(@PathVariable Long driverId) {
        List<DriverAvailabilityDTO> availabilities = driverAvailabilityService.getAvailabilityByDriverId(driverId);
        return ResponseEntity.ok(availabilities);
    }

    @PostMapping
    public ResponseEntity<DriverAvailabilityDTO> createAvailability(@RequestBody DriverAvailabilityDTO dto) {
        DriverAvailabilityDTO created = driverAvailabilityService.createAvailability(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverAvailabilityDTO> updateAvailability(@PathVariable Long id, @RequestBody DriverAvailabilityDTO dto) {
        DriverAvailabilityDTO updated = driverAvailabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }
}
