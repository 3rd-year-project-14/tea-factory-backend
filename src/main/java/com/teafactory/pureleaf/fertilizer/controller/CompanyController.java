package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.CompanyCreateDTO;
import com.teafactory.pureleaf.fertilizer.entity.Company;
import com.teafactory.pureleaf.fertilizer.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateDTO companyCreateDTO) {
        Company createdCompany = companyService.createCompany(companyCreateDTO);
        return ResponseEntity.ok(createdCompany);
    }
}

