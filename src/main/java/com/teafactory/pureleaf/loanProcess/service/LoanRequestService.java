package com.teafactory.pureleaf.loanProcess.service;

import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestCreateDTO;
import com.teafactory.pureleaf.loanProcess.dto.LoanRequestResponseDTO;
import com.teafactory.pureleaf.loanProcess.entity.LoanRequest;
import com.teafactory.pureleaf.loanProcess.repository.LoanRequestRepository;
import com.teafactory.pureleaf.supplier.entity.Supplier;
import com.teafactory.pureleaf.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanRequestService {
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private SupplierRepository supplierRepository;

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
}
