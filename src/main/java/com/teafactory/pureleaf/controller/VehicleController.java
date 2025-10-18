package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.VehicleDTO;
import com.teafactory.pureleaf.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{vehicleNo}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable String vehicleNo) {
        return vehicleService.getVehicleById(vehicleNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{vehicleNo}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable String vehicleNo, @RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.updateVehicle(vehicleNo, vehicleDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{vehicleNo}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vehicleNo) {
        vehicleService.deleteVehicle(vehicleNo);
        return ResponseEntity.noContent().build();
    }
}
