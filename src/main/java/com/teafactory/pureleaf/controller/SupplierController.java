package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.SupplierDTO;
import com.teafactory.pureleaf.entity.Supplier;
import com.teafactory.pureleaf.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllSuppliers() {
        try {
            List<Supplier> suppliers = supplierRepository.findAll();
            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        try {
            Supplier supplier = supplierRepository.findById(id).orElse(null);
            if (supplier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found");
            }
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-user")
    public ResponseEntity<?> getSupplierByUserId(@RequestParam("userId") Long userId) {
        try {
            Supplier supplier = supplierRepository.findByUser_Id(userId);
            if (supplier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found for userId: " + userId);
            }
            // Map Supplier entity to SupplierDTO
            SupplierDTO dto = new SupplierDTO();
            dto.setFactoryId(supplier.getFactory() != null ? supplier.getFactory().getFactoryId() : null);
            dto.setRouteId(supplier.getRoute() != null ? supplier.getRoute().getRouteId() : null);
            dto.setLandSize(supplier.getLandSize());
            dto.setLandLocation(supplier.getLandLocation());
            dto.setPickupLocation(supplier.getPickupLocation());
            dto.setNicImage(supplier.getNicImage());
            dto.setApprovedDate(supplier.getApprovedDate());
            dto.setIsActive(supplier.getIsActive());
            dto.setSupplierId(supplier.getSupplierId());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
