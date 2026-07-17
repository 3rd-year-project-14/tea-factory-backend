// OOP Concept: Package (Encapsulation, Organization)
package com.teafactory.pureleaf.fertilizer.service;

// OOP Concept: Importing Classes (Abstraction, Reusability)
import com.teafactory.pureleaf.fertilizer.dto.FertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.dto.FertilizerCategoryDTO;
import com.teafactory.pureleaf.fertilizer.dto.SimpleFertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@Service
@RequiredArgsConstructor
public class FertilizerCompanyService {
    // OOP Concept: Dependency Injection (Abstraction, Encapsulation)
    private final FertilizerCompanyRepository companyRepo;
    private final FertilizerCategoryRepository categoryRepo;

    // OOP Concept: Method Declaration (Encapsulation)
    public FertilizerCompanyDTO createCompany(FertilizerCompanyDTO dto) {
        // OOP Concept: Polymorphism (Exception Handling)
        Optional<FertilizerCompany> existing = companyRepo.findByNameIgnoreCase(dto.getName());
        if (existing.isPresent()) {
            throw new RuntimeException("A company with this name already exists");
        }
        // OOP Concept: Object Instantiation
        FertilizerCompany company = new FertilizerCompany();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactNumber(dto.getContactNumber());
        company.setEmail(dto.getEmail());

        // OOP Concept: Collection Usage (Abstraction)
        List<String> categoryNames = dto.getCategories() != null ? dto.getCategories() : Collections.emptyList();
        Set<FertilizerCategory> categories = new HashSet<>();
        for (String categoryName : categoryNames) {
            FertilizerCategory category = categoryRepo.findByNameIgnoreCase(categoryName)
                    .orElseGet(() -> {
                        FertilizerCategory newCat = new FertilizerCategory();
                        newCat.setName(categoryName);
                        return categoryRepo.save(newCat);
                    });
            categories.add(category);
        }
        company.setCategories(categories);

        // OOP Concept: Object Instantiation
        FertilizerCompany saved = companyRepo.save(company);
        return mapToDTO(saved);
    }

    // OOP Concept: Method Declaration (Encapsulation)
    public FertilizerCompanyDTO updateCompany(Long id, FertilizerCompanyDTO dto) {
        // OOP Concept: Polymorphism (Exception Handling)
        FertilizerCompany company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactNumber(dto.getContactNumber());
        company.setEmail(dto.getEmail());

        // OOP Concept: Collection Usage (Abstraction)
        List<String> categoryNames = dto.getCategories() != null ? dto.getCategories() : Collections.emptyList();
        Set<FertilizerCategory> categories = new HashSet<>();
        for (String categoryName : categoryNames) {
            FertilizerCategory category = categoryRepo.findByNameIgnoreCase(categoryName)
                    .orElseGet(() -> {
                        FertilizerCategory newCat = new FertilizerCategory();
                        newCat.setName(categoryName);
                        return categoryRepo.save(newCat);
                    });
            categories.add(category);
        }
        company.setCategories(categories);

        // OOP Concept: Method Invocation
        return mapToDTO(companyRepo.save(company));
    }

    // OOP Concept: Method Declaration (Encapsulation)
    public List<FertilizerCompanyDTO> getAllCompanies() {
        // OOP Concept: Stream API (Abstraction)
        return companyRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // OOP Concept: Method Declaration (Encapsulation)
    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

    // --- Dropdown Support Methods ---
    // OOP Concept: Method Declaration (Encapsulation)
    public List<SimpleFertilizerCompanyDTO> getCompanyDropdown() {
        return companyRepo.findAll().stream()
                .map(c -> SimpleFertilizerCompanyDTO.builder().id(c.getId()).name(c.getName()).build())
                .collect(Collectors.toList());
    }

    // OOP Concept: Method Declaration (Encapsulation)
    public List<FertilizerCategoryDTO> getCategoriesByCompany(Long companyId) {
        FertilizerCompany company = companyRepo.findWithCategoriesById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Set<FertilizerCategory> assigned = company.getCategories();
        // Fallback: if no categories assigned to this company, return all categories instead of empty
        if (assigned == null || assigned.isEmpty()) {
            return categoryRepo.findAll().stream()
                    .map(cat -> FertilizerCategoryDTO.builder().id(cat.getId()).name(cat.getName()).build())
                    .sorted(Comparator.comparing(FertilizerCategoryDTO::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        }
        return assigned.stream()
                .map(cat -> FertilizerCategoryDTO.builder().id(cat.getId()).name(cat.getName()).build())
                .sorted(Comparator.comparing(FertilizerCategoryDTO::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // OOP Concept: Method Declaration (Encapsulation)
    public List<SimpleFertilizerCompanyDTO> getCompaniesByCategory(Long categoryId) {
        if (categoryId == null) throw new IllegalArgumentException("categoryId is required");
        FertilizerCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return companyRepo.findAllByCategoryId(category.getId()).stream()
                .sorted(Comparator.comparing(FertilizerCompany::getName, String.CASE_INSENSITIVE_ORDER))
                .map(c -> SimpleFertilizerCompanyDTO.builder().id(c.getId()).name(c.getName()).build())
                .collect(Collectors.toList());
    }
//dropdown support methods end
    // OOP Concept: Private Method (Encapsulation)
    private FertilizerCompanyDTO mapToDTO(FertilizerCompany company) {
        return FertilizerCompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .contactPerson(company.getContactPerson())
                .contactNumber(company.getContactNumber())
                .email(company.getEmail())
                .categories(company.getCategories().stream()
                        .map(FertilizerCategory::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
