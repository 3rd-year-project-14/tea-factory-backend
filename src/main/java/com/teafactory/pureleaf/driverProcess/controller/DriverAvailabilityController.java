package com.teafactory.pureleaf.driverProcess.controller;

import com.teafactory.pureleaf.driverProcess.dto.DriverAvailabilityDTO;
import com.teafactory.pureleaf.driverProcess.service.DriverAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
        dto.setDate(java.time.LocalDate.now()); // Set date to current date
        DriverAvailabilityDTO created = driverAvailabilityService.createAvailability(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverAvailabilityDTO> updateAvailability(@PathVariable Long id, @RequestBody DriverAvailabilityDTO dto) {
        DriverAvailabilityDTO updated = driverAvailabilityService.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/today/{driverId}")
    public ResponseEntity<DriverAvailabilityDTO> getTodayAvailability(@PathVariable Long driverId) {
        DriverAvailabilityDTO availability = driverAvailabilityService.getTodayAvailability(driverId);
        if (availability != null) {
            return ResponseEntity.ok(availability);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
