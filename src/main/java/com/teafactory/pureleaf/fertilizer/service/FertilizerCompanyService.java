package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.fertilizer.dto.FertilizerCompanyDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FertilizerCompanyService {
    private final FertilizerCompanyRepository companyRepo;
    private final FertilizerCategoryRepository categoryRepo;

    public FertilizerCompanyDTO createCompany(FertilizerCompanyDTO dto) {
        Optional<FertilizerCompany> existing = companyRepo.findByNameIgnoreCase(dto.getName());
        if (existing.isPresent()) {
            throw new RuntimeException("A company with this name already exists");
        }
        FertilizerCompany company = new FertilizerCompany();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactNumber(dto.getContactNumber());
        company.setEmail(dto.getEmail());

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

        FertilizerCompany saved = companyRepo.save(company);
        return mapToDTO(saved);
    }

    public FertilizerCompanyDTO updateCompany(Long id, FertilizerCompanyDTO dto) {
        FertilizerCompany company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactNumber(dto.getContactNumber());
        company.setEmail(dto.getEmail());

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

        return mapToDTO(companyRepo.save(company));
    }

    public List<FertilizerCompanyDTO> getAllCompanies() {
        return companyRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

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
