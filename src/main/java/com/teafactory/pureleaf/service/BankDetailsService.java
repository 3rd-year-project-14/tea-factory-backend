package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.BankDetailsDTO;
import com.teafactory.pureleaf.entity.BankDetails;
import com.teafactory.pureleaf.repository.BankDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankDetailsService {
    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    public List<BankDetailsDTO> getAllBankDetails() {
        List<BankDetails> bankDetailsList = bankDetailsRepository.findAll();
        return bankDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BankDetailsDTO getBankDetailsById(Long id) {
        return bankDetailsRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    private BankDetailsDTO convertToDTO(BankDetails bankDetails) {
        BankDetailsDTO dto = new BankDetailsDTO();
        dto.setBankDetailsId(bankDetails.getBankDetailsId());
        dto.setAccountNumber(bankDetails.getAccountNumber());
        dto.setBankName(bankDetails.getBankName());
        dto.setBranch(bankDetails.getBranch());
        dto.setAccountHolderName(bankDetails.getAccountHolderName());
        if (bankDetails.getUser() != null) {
            dto.setUserId(bankDetails.getUser().getId());
        }
        return dto;
    }
}

