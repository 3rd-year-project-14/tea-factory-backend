package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.entity.Supplier;
import com.teafactory.pureleaf.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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
}
