package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.FertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.dto.FertilizerCategoryDTO;
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

    @PutMapping("/{id}")
    public FertilizerCompanyDTO update(@PathVariable Long id, @RequestBody FertilizerCompanyDTO dto) {
        return companyService.updateCompany(id, dto);
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

    @GetMapping("/{id}/categories")
    public List<FertilizerCategoryDTO> categoriesByCompany(@PathVariable Long id) {
        return companyService.getCategoriesByCompany(id);
    }
}
