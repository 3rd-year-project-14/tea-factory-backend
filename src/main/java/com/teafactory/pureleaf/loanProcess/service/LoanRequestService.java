package com.teafactory.pureleaf.loanProcess.service;

import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestCreateDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestResponseDTO;
import com.teafactory.pureleaf.loanProcess.entity.Loan;
import com.teafactory.pureleaf.loanProcess.entity.LoanRate;
import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import com.teafactory.pureleaf.loanProcess.repository.LoanRepository;
import com.teafactory.pureleaf.loanProcess.repository.LoanRateRepository;
import com.teafactory.pureleaf.loanProcess.repository.LoanRequestRepository;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanRequestService {
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanRateRepository loanRateRepository;

    public LoanRequest createLoanRequest(LoanRequestCreateDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + dto.getSupplierId()));
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setSupplier(supplier);
        loanRequest.setAmount(dto.getAmount());
        loanRequest.setMonths(dto.getMonths());
        loanRequest.setDate(LocalDate.now());
        loanRequest.setType("loan");
        loanRequest.setStatus(LoanRequest.Status.PENDING);
        return loanRequestRepository.save(loanRequest);
    }

    public List<LoanRequestResponseDTO> getAllLoanRequests() {
        return loanRequestRepository.findAll().stream().map(lr -> new LoanRequestResponseDTO(
                lr.getReqId(),
                lr.getSupplier().getSupplierId(),
                lr.getAmount(),
                lr.getMonths(),
                lr.getDate(),
                lr.getStatus()
        )).toList();
    }

    @Transactional
    public Loan approveLoanRequest(Long reqId) {
        LoanRequest loanRequest = loanRequestRepository.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found with id: " + reqId));
        if (loanRequest.getStatus() != LoanRequest.Status.PENDING) {
            throw new IllegalStateException("Loan request is not pending");
        }
        // Set status to APPROVED
        loanRequest.setStatus(LoanRequest.Status.APPROVED);
        loanRequestRepository.save(loanRequest);

        // Get active rate for the request date
        LoanRate loanRate = loanRateRepository
            .findFirstByEffectiveDateLessThanEqualAndStatusTrueOrderByEffectiveDateDesc(loanRequest.getDate())
            .orElseThrow(() -> new ResourceNotFoundException("No active loan rate found for the request date"));

        // Calculate monthly installment: (P * (1 + r/100)) / n
        BigDecimal principal = loanRequest.getAmount();
        BigDecimal rate = loanRate.getRate().divide(BigDecimal.valueOf(100));
        int months = loanRequest.getMonths();
        BigDecimal total = principal.add(principal.multiply(rate));
        BigDecimal monthlyInstalment = total.divide(BigDecimal.valueOf(months), 2, BigDecimal.ROUND_HALF_UP);

        Loan loan = new Loan();
        loan.setSupplier(loanRequest.getSupplier());
        loan.setLoanAmount(principal);
        loan.setDate(java.time.LocalDate.now());
        loan.setMonths(months);
        loan.setLoanRate(loanRate);
        loan.setMonthlyInstalment(monthlyInstalment);
        loan.setRemainingAmount(total);
        // status is set by entity logic
        loanRepository.save(loan);
        return loan;
    }
}
