package com.teafactory.pureleaf.supplier.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.teafactory.pureleaf.supplier.dto.*;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.entity.SupplierRequest;
import com.teafactory.pureleaf.supplier.service.SupplierRequestService;
import com.teafactory.pureleaf.supplier.service.SupplierService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/supplier-requests")
public class SupplierRequestController {

    @Autowired
    private SupplierRequestService supplierRequestService;

    @Autowired
    private SupplierService supplierService;

    @PostMapping()
    public ResponseEntity<?> createSupplierRequest(
            @RequestPart("supplierRequest") String supplierRequestStr,
            @RequestPart(value = "nicImage") MultipartFile nicImageFile) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            CreateSupplierRequestDTO createSupplierRequestDTO = objectMapper.readValue(supplierRequestStr, CreateSupplierRequestDTO.class);

            // Create supplier request without NIC image
            Long createdRequestId = supplierRequestService.createSupplierRequest(createSupplierRequestDTO);

            // Upload NIC image if provided
            if (nicImageFile != null && !nicImageFile.isEmpty()) {
                supplierRequestService.saveNicImage(createdRequestId, nicImageFile);
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
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
    public ResponseEntity<?> approveSupplierRequest(@PathVariable("id") Long supplierRequestId, @RequestBody ApproveSupplierRequestDTO dto) {

        supplierService.approveSupplierRequest(supplierRequestId, dto);
        return ResponseEntity.ok().build();
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

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<SupplierRequestStatusDTO> getSupplierRequestStatus(@PathVariable Long userId) {
            SupplierRequestStatusDTO requestStatus = supplierRequestService.getSupplierRequestStatus(userId);
            return new ResponseEntity<>(requestStatus, HttpStatus.OK);
    }
}
