package com.teafactory.pureleaf.loanProcess.controller;

import com.teafactory.pureleaf.loanProcess.dto.LoanRequestCreateDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestResponseDTO;
import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import com.teafactory.pureleaf.loanProcess.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<LoanRequestResponseDTO>> getAllLoanRequests() {
        List<LoanRequestResponseDTO> list = loanRequestService.getAllLoanRequests();
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
}
