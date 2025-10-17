package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.fertilizer.dto.CreateFertilizerStockDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerStock;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerStockRepository;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FertilizerStockService {
    private final FertilizerStockRepository stockRepository;
    private final FertilizerCategoryRepository categoryRepository;
    private final FertilizerCompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public FertilizerStock addStock(CreateFertilizerStockDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));
        FertilizerCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));
        FertilizerCompany company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        FertilizerStock stock = FertilizerStock.builder()
                .user(user)
                .category(category)
                .company(company)
                .weightPerQuantity(dto.getWeightPerQuantity())
                .purchasePrice(dto.getPurchasePrice())
                .sellPrice(dto.getSellPrice())
                .warehouse(dto.getWarehouse())
                .build();
        return stockRepository.save(stock);
    }
}
