package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.SupplierRequestDTO;
import com.teafactory.pureleaf.entity.Supplier;
import com.teafactory.pureleaf.entity.SupplierRequest;
import com.teafactory.pureleaf.service.SupplierRequestService;
import com.teafactory.pureleaf.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/supplier-requests")
public class SupplierRequestController {

    @Autowired
    private SupplierRequestService supplierRequestService;

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/")
    ResponseEntity<?> createSupplierRequest(@RequestBody SupplierRequestDTO supplierRequest){
        try {
            supplierRequestService.createSupplierRequest(supplierRequest);
            return new ResponseEntity<>(supplierRequest, HttpStatus.CREATED);
        }catch (Exception e) {
            return  ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplierRequest(@PathVariable Long id, @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            supplierRequestService.updateSupplierRequest(id, supplierRequestDTO);
            return new ResponseEntity<>(supplierRequestDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierRequest(@PathVariable Long id) {
        try {
            SupplierRequest supplierRequest = supplierRequestService.getSupplierRequestById(id);
            return new ResponseEntity<>(supplierRequest, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSupplierRequests() {
        try {
            List<SupplierRequest> requests = supplierRequestService.getAllSupplierRequests();
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveSupplierRequest(@PathVariable Long id, @RequestParam Long routeId) {
        try {
            Supplier supplier = supplierService.approveSupplierRequest(id, routeId);
            if (supplier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier request not found");
            }
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectSupplierRequest(@PathVariable Long id, @RequestParam(required = false) String reason) {
        try {
            SupplierRequest rejected = supplierRequestService.rejectSupplierRequest(id, reason);
            return new ResponseEntity<>(rejected, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
