package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.TripSupplierDTO;
import com.teafactory.pureleaf.service.TripSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trip-suppliers")
public class TripSupplierController {
    @Autowired
    private TripSupplierService tripSupplierService;

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<TripSupplierDTO>> getSuppliersByTripId(@PathVariable Long tripId) {
        List<TripSupplierDTO> suppliers = tripSupplierService.getSuppliersByTripId(tripId);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping
    public ResponseEntity<List<TripSupplierDTO>> getAllTripSuppliers() {
        List<TripSupplierDTO> all = tripSupplierService.getAllTripSuppliers();
        return ResponseEntity.ok(all);
    }
}
