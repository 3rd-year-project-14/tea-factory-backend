package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.BankDetailsDTO;
import com.teafactory.pureleaf.service.BankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-details")
public class BankDetailsController {
    @Autowired
    private BankDetailsService bankDetailsService;

    @GetMapping
    public List<BankDetailsDTO> getAllBankDetails() {
        return bankDetailsService.getAllBankDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDetailsDTO> getBankDetailsById(@PathVariable Long id) {
        BankDetailsDTO dto = bankDetailsService.getBankDetailsById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
