package com.teafactory.pureleaf.supplier.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.teafactory.pureleaf.supplier.dto.SupplierDetailsDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierRequestDTO;
import com.teafactory.pureleaf.supplier.dto.RequestSuppliersDTO;
import com.teafactory.pureleaf.supplier.dto.SupplierRequestDetailsDTO;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.service.SupplierRequestService;
import com.teafactory.pureleaf.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/supplier-requests")
public class SupplierRequestController {

    @Autowired
    private SupplierRequestService supplierRequestService;

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/")
    public ResponseEntity<?> createSupplierRequest(
            @RequestPart("supplierRequest") String supplierRequestStr,
            @RequestPart(value = "nicImage", required = false) MultipartFile nicImageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SupplierRequestDTO supplierRequest = objectMapper.readValue(supplierRequestStr, SupplierRequestDTO.class);

            if (nicImageFile != null && !nicImageFile.isEmpty()) {
                String imageUrl = supplierRequestService.saveNicImageFile(nicImageFile);
                supplierRequest.setNicImage(imageUrl);
            }
            supplierRequestService.createSupplierRequest(supplierRequest);
            return new ResponseEntity<>(supplierRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @GetMapping(params = "userId")
    public ResponseEntity<?> getSupplierRequestsByUserId(@RequestParam("userId") Long userId) {
        try {
            List<SupplierRequest> requests = supplierRequestService.getSupplierRequestsByUserId(userId);
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveSupplierRequest(@PathVariable Long id, @RequestParam Long routeId, @RequestParam(required = false) Integer initialBagCount) {
        try {
            Supplier supplier = supplierService.approveSupplierRequest(id, routeId, initialBagCount);
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

    @PostMapping("/{id}/upload-nic-image")
    public ResponseEntity<?> uploadNicImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imagePath = supplierRequestService.saveNicImage(id, file);
            return ResponseEntity.ok(imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/factory/{factoryId}/status/{status}")
    public ResponseEntity<?> getRequestsByFactoryIdAndStatus(@PathVariable Long factoryId, @PathVariable String status) {
            List<RequestSuppliersDTO> requests = supplierRequestService.getRequestsByFactoryIdAndStatus(factoryId, status);
            return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/details/{requestId}")
    public ResponseEntity<SupplierRequestDetailsDTO> getSupplierRequestDetails(@PathVariable Long requestId) {
        SupplierRequestDetailsDTO requestDetails = supplierRequestService.getSupplierRequestDetails(requestId);
        return new ResponseEntity<>(requestDetails, HttpStatus.OK);
    }
}
