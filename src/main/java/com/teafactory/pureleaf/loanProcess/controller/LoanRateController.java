package com.teafactory.pureleaf.loanProcess.controller;

import com.teafactory.pureleaf.loanProcess.dto.LoanRateRequest;
import com.teafactory.pureleaf.loanProcess.entity.LoanRate;
import com.teafactory.pureleaf.loanProcess.service.LoanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-rate")
public class LoanRateController {
    @Autowired
    private LoanRateService loanRateService;

    @PostMapping
    public ResponseEntity<LoanRate> createLoanRate(@RequestBody LoanRateRequest request) {
        LoanRate createdLoanRate = loanRateService.createLoanRate(request);
        return ResponseEntity.ok(createdLoanRate);
    }

    @GetMapping
    public ResponseEntity<List<LoanRate>> getAllLoanRates() {
        List<LoanRate> loanRates = loanRateService.getAllLoanRates();
        return ResponseEntity.ok(loanRates);
    }
}
