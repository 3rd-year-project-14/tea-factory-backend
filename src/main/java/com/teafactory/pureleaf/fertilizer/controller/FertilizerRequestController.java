package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.*;
import com.teafactory.pureleaf.fertilizer.service.FertilizerRequestService;
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
        log.info("Received request to create fertilizer request for user: {}", dto.getUserId());
        FertilizerRequestDTO created = fertilizerRequestService.createRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Create multiple fertilizer requests in batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<FertilizerRequestDTO>> createBatchRequests(@Valid @RequestBody BatchFertilizerRequestDTO batchDto) {
        log.info("Received batch request to create {} fertilizer requests", batchDto.getRequests().size());
        List<FertilizerRequestDTO> created = fertilizerRequestService.createBatchRequests(batchDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
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
     * Get a specific fertilizer request by ID
     */
    @GetMapping("/{id}")
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
}
