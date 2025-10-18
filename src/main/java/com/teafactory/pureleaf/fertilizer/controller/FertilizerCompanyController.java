package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.FertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.dto.SimpleFertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.service.FertilizerCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-companies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FertilizerCompanyController {

    private final FertilizerCompanyService companyService;

    @PostMapping
    public FertilizerCompanyDTO create(@RequestBody FertilizerCompanyDTO dto) {
        return companyService.createCompany(dto);
    }

    @GetMapping
    public List<FertilizerCompanyDTO> getAll() {
        return companyService.getAllCompanies();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

    // --- Dropdown Endpoints ---
    @GetMapping("/dropdown")
    public List<SimpleFertilizerCompanyDTO> companyDropdown() {
        return companyService.getCompanyDropdown();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<SimpleFertilizerCompanyDTO> companiesByCategory(@PathVariable Long categoryId) {
        return companyService.getCompaniesByCategory(categoryId);
    }
}
