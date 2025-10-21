package com.teafactory.pureleaf.loanProcess.controller;

import com.teafactory.pureleaf.loanProcess.dto.LoanResponseDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanSimpleDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanStatsDTO;
import com.teafactory.pureleaf.loanProcess.entity.Loan;
import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import com.teafactory.pureleaf.loanProcess.repository.LoanRepository;
import com.teafactory.pureleaf.loanProcess.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

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

    @GetMapping("/stats")
    public ResponseEntity<LoanStatsDTO> getLoanStats(
            @RequestParam Long factoryId,
            @RequestParam int month,
            @RequestParam int year) {
        long completedLoanCount = loanRepository.countByStatusAndFactoryAndMonthAndYear(Loan.Status.COMPLETE, factoryId, month, year);
        BigDecimal completedLoanTotal = loanRepository.sumByStatusAndFactoryAndMonthAndYear(Loan.Status.COMPLETE, factoryId, month, year);
        long remainingLoanCount = loanRepository.countByStatusAndFactoryAndMonthAndYear(Loan.Status.REMAINING, factoryId, month, year);
        BigDecimal remainingLoanTotal = loanRepository.sumByStatusAndFactoryAndMonthAndYear(Loan.Status.REMAINING, factoryId, month, year);
        long pendingLoanRequestCount = loanRequestRepository.countByStatusAndFactoryAndMonthAndYear(LoanRequest.Status.PENDING, factoryId, month, year);
        BigDecimal pendingLoanRequestTotal = loanRequestRepository.sumByStatusAndFactoryAndMonthAndYear(LoanRequest.Status.PENDING, factoryId, month, year);
        LoanStatsDTO stats = new LoanStatsDTO(
                completedLoanCount,
                completedLoanTotal,
                remainingLoanCount,
                remainingLoanTotal,
                pendingLoanRequestCount,
                pendingLoanRequestTotal
        );
        return ResponseEntity.ok(stats);
    }

    /**
     * Allowed status values: COMPLETE, REMAINING
     */
    @GetMapping("/filter")
    public ResponseEntity<?> getLoansByFactoryStatusMonthYear(
        @RequestParam Long factoryId,
        @RequestParam String status,
        @RequestParam int month,
        @RequestParam int year) {
        Loan.Status loanStatus;
        try {
            loanStatus = Loan.Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value. Allowed values: COMPLETE, REMAINING");
        }
        List<LoanSimpleDTO> loans = loanRepository
            .findByStatusAndFactoryAndMonthAndYear(loanStatus, factoryId, month, year)
            .stream()
            .map(loan -> new LoanSimpleDTO(
                loan.getSupplier().getUser().getName(),
                loan.getLoanId(),
                loan.getLoanAmount(),
                loan.getMonths(),
                loan.getDate(),
                loan.getMonthlyInstalment()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }
}
