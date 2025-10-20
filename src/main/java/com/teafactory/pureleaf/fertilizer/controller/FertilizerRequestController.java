package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.*;
import com.teafactory.pureleaf.fertilizer.service.FertilizerRequestService;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerRequestStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class FertilizerRequestController {

    private final FertilizerRequestService fertilizerRequestService;

    /**
     * Create a single fertilizer request
     */
    @PostMapping
    public ResponseEntity<FertilizerRequestDTO> createRequest(@Valid @RequestBody CreateFertilizerRequestDTO dto) {
        log.info("Received request to create fertilizer request for fertilizerStockId: {}", dto.getFertilizerStockId());
        FertilizerRequestDTO created = fertilizerRequestService.createRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Create multiple fertilizer requests in batch (single parent request)
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchFertilizerRequestResponseDTO> createBatchRequests(@Valid @RequestBody BatchFertilizerRequestDTO batchDto) {
        log.info("Received batch request to create {} fertilizer requests (single parent)", batchDto.getRequests().size());
        BatchFertilizerRequestResponseDTO response = fertilizerRequestService.createBatchRequestSingleParent(batchDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all fertilizer requests with optional status filter
     */
    @GetMapping
    public ResponseEntity<List<FertilizerRequestDTO>> getAllRequests(
            @RequestParam(value = "status", required = false) String status) {
        log.info("Fetching all fertilizer requests with status filter: {}", status);
        List<FertilizerRequestDTO> requests = fertilizerRequestService.getAllRequests(status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get all batch fertilizer requests (parent + items)
     */
    @GetMapping("/batch")
    public ResponseEntity<List<BatchFertilizerRequestResponseDTO>> getAllBatchRequests() {
        log.info("Fetching all batch fertilizer requests");
        List<BatchFertilizerRequestResponseDTO> responses = fertilizerRequestService.getAllBatchRequests();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get a batch fertilizer request (parent + items) by parent request ID
     */
    @GetMapping("/batch/{id}")
    public ResponseEntity<BatchFertilizerRequestResponseDTO> getBatchRequestById(@PathVariable Long id) {
        log.info("Fetching batch fertilizer request with parent ID: {}", id);
        BatchFertilizerRequestResponseDTO response = fertilizerRequestService.getBatchRequestById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific fertilizer request by ID
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<FertilizerRequestDTO> getRequestById(@PathVariable Long id) {
        log.info("Fetching fertilizer request with ID: {}", id);
        FertilizerRequestDTO request = fertilizerRequestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    /**
     * Get all fertilizer requests for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FertilizerRequestDTO>> getRequestsByUser(@PathVariable Long userId) {
        log.info("Fetching fertilizer requests for user: {}", userId);
        List<FertilizerRequestDTO> requests = fertilizerRequestService.getRequestsByUser(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get fertilizer requests for a specific user with status filter
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<FertilizerRequestDTO>> getRequestsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        log.info("Fetching fertilizer requests for user: {} with status: {}", userId, status);
        List<FertilizerRequestDTO> requests = fertilizerRequestService.getRequestsByUserAndStatus(userId, status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Update the status of a fertilizer request (approve, reject, fulfill)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<FertilizerRequestDTO> updateRequestStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFertilizerRequestStatusDTO dto) {
        log.info("Updating status of fertilizer request {} to: {}", id, dto.getStatus());
        FertilizerRequestDTO updated = fertilizerRequestService.updateRequestStatus(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Approve a fertilizer request (set status to APPROVED)
     */
    @PatchMapping("/{id}/approve")
    public ResponseEntity<FertilizerRequestDTO> approveRequest(@PathVariable Long id) {
        UpdateFertilizerRequestStatusDTO dto = UpdateFertilizerRequestStatusDTO.builder()
                .status(FertilizerRequestStatus.APPROVED)
                .build();
        FertilizerRequestDTO updated = fertilizerRequestService.updateRequestStatus(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Reject a fertilizer request (set status to REJECTED with reason)
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<FertilizerRequestDTO> rejectRequest(@PathVariable Long id, @RequestBody RejectRequestDTO rejectDto) {
        UpdateFertilizerRequestStatusDTO dto = UpdateFertilizerRequestStatusDTO.builder()
                .status(FertilizerRequestStatus.REJECTED)
                .rejectReason(rejectDto.getRejectReason())
                .build();
        FertilizerRequestDTO updated = fertilizerRequestService.updateRequestStatus(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Create a new fertilizer stock request (custom endpoint)
     */
    @PostMapping("/fertilizer-stock-requests")
    public ResponseEntity<FertilizerRequestDTO> createFertilizerStockRequest(@Valid @RequestBody NewFertilizerStockRequestDTO dto) {
        log.info("Received new fertilizer stock request: categoryId={}, companyId={}, userId={}, quantity={}", dto.getCategoryId(), dto.getCompanyId(), dto.getUserId(), dto.getQuantity());
        FertilizerRequestDTO created = fertilizerRequestService.createFertilizerStockRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get all fertilizer stock requests (custom endpoint)
     */
    @GetMapping("/fertilizer-stock-requests")
    public ResponseEntity<List<FertilizerRequestDTO>> getAllFertilizerStockRequests() {
        log.info("Fetching all fertilizer stock requests");
        List<FertilizerRequestDTO> requests = fertilizerRequestService.getAllFertilizerStockRequests();
        return ResponseEntity.ok(requests);
    }
}
