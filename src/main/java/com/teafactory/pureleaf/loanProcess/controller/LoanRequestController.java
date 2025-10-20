package com.teafactory.pureleaf.loanProcess.controller;

import com.teafactory.pureleaf.loanProcess.dto.LoanRequestCreateDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestResponseDTO;
import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import com.teafactory.pureleaf.loanProcess.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {
    @Autowired
    private LoanRequestService loanRequestService;

    @PostMapping
    public ResponseEntity<LoanRequestResponseDTO> createLoanRequest(@RequestBody LoanRequestCreateDTO dto) {
        LoanRequest created = loanRequestService.createLoanRequest(dto);
        LoanRequestResponseDTO response = new LoanRequestResponseDTO(
            created.getReqId(),
            created.getSupplier().getSupplierId(),
            created.getAmount(),
            created.getMonths(),
            created.getDate(),
            created.getStatus()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<LoanRequestResponseDTO>> createLoanRequests(@RequestBody List<LoanRequestCreateDTO> dtos) {
        List<LoanRequestResponseDTO> responses = dtos.stream()
            .map(dto -> {
                LoanRequest created = loanRequestService.createLoanRequest(dto);
                return new LoanRequestResponseDTO(
                    created.getReqId(),
                    created.getSupplier().getSupplierId(),
                    created.getAmount(),
                    created.getMonths(),
                    created.getDate(),
                    created.getStatus()
                );
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<LoanRequestResponseDTO>> getAllLoanRequests() {
        List<LoanRequestResponseDTO> list = loanRequestService.getAllLoanRequests();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<LoanRequestResponseDTO>> getLoanRequestsBySupplierId(@PathVariable Long supplierId) {
        List<LoanRequestResponseDTO> list = loanRequestService.getLoanRequestsBySupplierId(supplierId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLoanRequest(@PathVariable("id") Long id) {
        try {
            loanRequestService.approveLoanRequest(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanRequestResponseDTO> updateLoanRequest(@PathVariable Long id, @RequestBody LoanRequestCreateDTO dto) {
        LoanRequest updated = loanRequestService.updateLoanRequest(id, dto);
        LoanRequestResponseDTO response = new LoanRequestResponseDTO(
            updated.getReqId(),
            updated.getSupplier().getSupplierId(),
            updated.getAmount(),
            updated.getMonths(),
            updated.getDate(),
            updated.getStatus()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoanRequest(@PathVariable Long id) {
        loanRequestService.deleteLoanRequest(id);
        return ResponseEntity.ok().build();
    }
}
