package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.dto.SimpleFertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.service.FertilizerCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FertilizerCategoryController {
    private final FertilizerCategoryRepository categoryRepository;
    private final FertilizerCompanyService companyService;

    @GetMapping
    public List<FertilizerCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}/companies")
    public List<SimpleFertilizerCompanyDTO> getCompaniesForCategory(@PathVariable Long id) {
        return companyService.getCompaniesByCategory(id);
    }
}
