package com.teafactory.pureleaf.loanProcess.controller;

import com.teafactory.pureleaf.loanProcess.dto.LoanResponseDTO;
import com.teafactory.pureleaf.loanProcess.entity.Loan;
import com.teafactory.pureleaf.loanProcess.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        List<LoanResponseDTO> loans = loanRepository.findAll().stream().map(loan -> new LoanResponseDTO(
                loan.getLoanId(),
                loan.getSupplier().getSupplierId(),
                loan.getLoanAmount(),
                loan.getDate(),
                loan.getMonths(),
                loan.getLoanRate().getRate(),
                loan.getMonthlyInstalment(),
                loan.getRemainingAmount(),
                loan.getStatus().name()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansBySupplierId(@PathVariable Long supplierId) {
        List<LoanResponseDTO> loans = loanRepository.findBySupplier_SupplierId(supplierId).stream()
                .filter(loan -> loan.getStatus() == Loan.Status.REMAINING)
                .map(loan -> new LoanResponseDTO(
                        loan.getLoanId(),
                        loan.getSupplier().getSupplierId(),
                        loan.getLoanAmount(),
                        loan.getDate(),
                        loan.getMonths(),
                        loan.getLoanRate().getRate(),
                        loan.getMonthlyInstalment(),
                        loan.getRemainingAmount(),
                        loan.getStatus().name()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }
}
