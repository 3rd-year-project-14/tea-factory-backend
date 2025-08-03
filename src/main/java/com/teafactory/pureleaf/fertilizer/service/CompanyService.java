package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.fertilizer.dto.CompanyCreateDTO;
import com.teafactory.pureleaf.fertilizer.dto.FertilizerTypeCreateDTO;
import com.teafactory.pureleaf.fertilizer.entity.Company;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerType;
import com.teafactory.pureleaf.fertilizer.repository.CompanyRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private FertilizerTypeRepository fertilizerTypeRepository;

    @Transactional
    public Company createCompany(CompanyCreateDTO dto) {
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setPhone(dto.getPhone());
        company.setEmail(dto.getEmail());

        Set<FertilizerType> fertilizerTypes = new HashSet<>();
        // Add existing fertilizer types by IDs
        if (dto.getFertilizerTypeIds() != null && !dto.getFertilizerTypeIds().isEmpty()) {
            fertilizerTypes.addAll(fertilizerTypeRepository.findAllById(dto.getFertilizerTypeIds()));
        }
        // Add new fertilizer types
        if (dto.getNewFertilizerTypes() != null && !dto.getNewFertilizerTypes().isEmpty()) {
            for (FertilizerTypeCreateDTO newTypeDto : dto.getNewFertilizerTypes()) {
                FertilizerType newType = new FertilizerType();
                newType.setName(newTypeDto.getName());
                newType.setDescription(newTypeDto.getDescription());
                fertilizerTypes.add(fertilizerTypeRepository.save(newType));
            }
        }
        company.setFertilizerTypes(fertilizerTypes);

        return companyRepository.save(company);
    }
}
