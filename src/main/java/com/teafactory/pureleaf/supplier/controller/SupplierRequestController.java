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
import org.springframework.data.domain.Pageable;
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

    // Handles creation of a new supplier request, including NIC image upload
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

    // Retrieves supplier requests for a specific user
    @GetMapping(params = "userId")
    public ResponseEntity<?> getSupplierRequestsByUserId(@RequestParam("userId") Long userId) {
            List<SupplierRequest> requests = supplierRequestService.getSupplierRequestsByUserId(userId);
            return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    // Approves a supplier request by ID
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveSupplierRequest(@PathVariable("id") Long supplierRequestId, @RequestBody ApproveSupplierRequestDTO dto) {
        supplierService.approveSupplierRequest(supplierRequestId, dto);
        return ResponseEntity.ok().build();
    }

    // Rejects a supplier request by ID
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectSupplierRequest(@PathVariable Long id, @RequestBody RejectSupplierRequestDTO r) {
        supplierService.rejectSupplierRequest(id, r);
        return ResponseEntity.ok().build();
    }

    // Retrieves supplier requests by factory ID and status with pagination, search, and sorting
    @GetMapping("/factory/{factoryId}/status/{status}")
    public ResponseEntity<?> getRequestsByFactoryIdAndStatus(
            @PathVariable Long factoryId,
            @PathVariable String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestedDate,desc") String[] sort
    ) {
        String sortBy = sort[0];
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page,
                size,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy)
        );
        var requests = supplierRequestService.getRequestsByFactoryIdAndStatus(factoryId, status, search, pageable);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    // Retrieves details of a specific supplier request
    @GetMapping("/details/{requestId}")
    public ResponseEntity<SupplierRequestDetailsDTO> getSupplierRequestDetails(@PathVariable Long requestId) {
        SupplierRequestDetailsDTO requestDetails = supplierRequestService.getSupplierRequestDetails(requestId);
        return new ResponseEntity<>(requestDetails, HttpStatus.OK);
    }

    // Retrieves the status of supplier requests for a specific user
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<SupplierRequestStatusDTO> getSupplierRequestStatus(@PathVariable Long userId) {
            SupplierRequestStatusDTO requestStatus = supplierRequestService.getSupplierRequestStatus(userId);
            return new ResponseEntity<>(requestStatus, HttpStatus.OK);
    }
}
