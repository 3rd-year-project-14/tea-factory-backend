// OOP Concept: Package (Encapsulation, Organization)
package com.teafactory.pureleaf.fertilizer.controller;

// OOP Concept: Importing Classes (Abstraction, Reusability)
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.dto.SimpleFertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.service.FertilizerCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@RestController
@RequestMapping("/api/fertilizer-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FertilizerCategoryController {
    // OOP Concept: Dependency Injection (Abstraction, Encapsulation)
    private final FertilizerCategoryRepository categoryRepository;
    private final FertilizerCompanyService companyService;

    // OOP Concept: Method Declaration (Encapsulation)
    @GetMapping
    public List<FertilizerCategory> getAllCategories() {
        // OOP Concept: Calling Repository Method (Abstraction)
        return categoryRepository.findAll();
    }

    // OOP Concept: Method Declaration (Encapsulation)
    @GetMapping("/{id}/companies")
    public List<SimpleFertilizerCompanyDTO> getCompaniesForCategory(@PathVariable Long id) {
        // OOP Concept: Calling Service Method (Abstraction)
        return companyService.getCompaniesByCategory(id);
    }
}
