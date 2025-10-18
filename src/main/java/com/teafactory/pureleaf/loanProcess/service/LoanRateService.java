package com.teafactory.pureleaf.loanProcess.service;

import com.teafactory.pureleaf.loanProcess.dto.LoanRateRequest;
import com.teafactory.pureleaf.loanProcess.entity.LoanRate;
import com.teafactory.pureleaf.loanProcess.repository.LoanRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanRateService {
    @Autowired
    private LoanRateRepository loanRateRepository;

    public LoanRate createLoanRate(LoanRateRequest request) {
        LoanRate loanRate = new LoanRate();
        loanRate.setRate(request.getRate());
        loanRate.setEffectiveDate(request.getEffectiveDate());
        loanRate.setStatus(request.isStatus());
        // createdAt and updatedAt are set by default in entity
        return loanRateRepository.save(loanRate);
    }

    public List<LoanRate> getAllLoanRates() {
        return loanRateRepository.findAll();
    }
}
