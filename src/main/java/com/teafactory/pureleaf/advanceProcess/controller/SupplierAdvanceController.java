package com.teafactory.pureleaf.advanceProcess.controller;

import com.teafactory.pureleaf.advanceProcess.dto.AdvanceApprovalDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceDetailsDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceRequestDto;
import com.teafactory.pureleaf.advanceProcess.dto.AdvanceResponseDto;
import com.teafactory.pureleaf.advanceProcess.service.SupplierAdvanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advances")
@RequiredArgsConstructor
public class SupplierAdvanceController {
    private final SupplierAdvanceService supplierAdvanceService;

    // 1. Create new advance request
    @PostMapping("/request")
    public ResponseEntity<AdvanceResponseDto> requestAdvance(@Valid @RequestBody AdvanceRequestDto requestDto) {
        AdvanceResponseDto response = supplierAdvanceService.createAdvanceRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get advance history for supplier
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<AdvanceResponseDto>> getSupplierAdvanceHistory(@PathVariable Long supplierId) {
        List<AdvanceResponseDto> advances = supplierAdvanceService.getAdvanceHistoryForSupplier(supplierId);
        return ResponseEntity.ok(advances);
    }

    // Get advance by ID

    @GetMapping("/{id}")
    public ResponseEntity<AdvanceDetailsDto> getAdvanceById(@PathVariable Long id) {
        AdvanceDetailsDto advance = supplierAdvanceService.getAdvanceById(id);
        return ResponseEntity.ok(advance);
    }

    @GetMapping("/{factoryId}/status-counts")
    public ResponseEntity<List<com.teafactory.pureleaf.advanceProcess.dto.AdvanceStatusCountDto>> getAdvanceStatusCounts(
            @PathVariable Long factoryId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        List<com.teafactory.pureleaf.advanceProcess.dto.AdvanceStatusCountDto> statusCounts = supplierAdvanceService.getAdvanceStatusCounts(factoryId, month, year);
        return ResponseEntity.ok(statusCounts);
    }

    // 3. Get requests for factory by status
    @GetMapping("/{factoryId}/status")
    public ResponseEntity<Page<AdvanceResponseDto>> getAdvancesForFactoryByStatus(
            @PathVariable Long factoryId,
            @RequestParam("status") String status,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "search", required = false) String search,
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
        Page<AdvanceResponseDto> advances = supplierAdvanceService.getAdvancesForFactoryByStatus(factoryId, status, month, year, search, pageable);
        return ResponseEntity.ok(advances);
    }

    // 4. Approve/Reject advance (manager only)
    @PreAuthorize("hasRole('FACTORY_MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AdvanceResponseDto> approveOrRejectAdvance(@PathVariable Long id, @Valid @RequestBody AdvanceApprovalDto approvalDto) {
        AdvanceResponseDto response = supplierAdvanceService.approveOrRejectAdvance(id, approvalDto, approvalDto.getApprovedByUserId());
        return ResponseEntity.ok(response);
    }

    // 6. Reject advance (manager only)

    @PutMapping("/{id}/reject")
    public ResponseEntity<AdvanceResponseDto> rejectAdvance(@PathVariable Long id, @Valid @RequestBody com.teafactory.pureleaf.advanceProcess.dto.AdvanceRejectionDto rejectionDto) {
        AdvanceResponseDto response = supplierAdvanceService.rejectAdvance(id, rejectionDto);
        return ResponseEntity.ok(response);
    }

    // 5. Mark as paid
    @PutMapping("/{id}/pay")
    public ResponseEntity<AdvanceResponseDto> markAdvanceAsPaid(@PathVariable Long id) {
        AdvanceResponseDto response = supplierAdvanceService.markAdvanceAsPaid(id);
        return ResponseEntity.ok(response);
    }
}
