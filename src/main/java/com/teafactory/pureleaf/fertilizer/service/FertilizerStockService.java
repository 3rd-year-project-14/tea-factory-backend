package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.exception.ResourceNotFoundException;
import com.teafactory.pureleaf.fertilizer.dto.CreateFertilizerStockDTO;
import com.teafactory.pureleaf.fertilizer.dto.FertilizerStockDisplayDTO;
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

import java.util.List;
import java.util.stream.Collectors;

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
                .quantity(dto.getQuantity())
                .build();
        return stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public java.util.List<com.teafactory.pureleaf.fertilizer.dto.FertilizerStockDTO> getAllStocks() {
        return stockRepository.findAll().stream().map(stock ->
            com.teafactory.pureleaf.fertilizer.dto.FertilizerStockDTO.builder()
                .id(stock.getId())
                .userId(stock.getUser() != null ? stock.getUser().getId() : null)
                .categoryId(stock.getCategory() != null ? stock.getCategory().getId() : null)
                .categoryName(stock.getCategory() != null ? stock.getCategory().getName() : null)
                .companyId(stock.getCompany() != null ? stock.getCompany().getId() : null)
                .companyName(stock.getCompany() != null ? stock.getCompany().getName() : null)
                .weightPerQuantity(stock.getWeightPerQuantity())
                .purchasePrice(stock.getPurchasePrice())
                .sellPrice(stock.getSellPrice())
                .warehouse(stock.getWarehouse())
                .quantity(stock.getQuantity())
                .createdAt(stock.getCreatedAt())
                .build()
        ).toList();
    }

    @Transactional
    public FertilizerStock updateStock(Long id, CreateFertilizerStockDTO dto) {
        FertilizerStock stock = stockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fertilizer stock not found: " + id));
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));
        FertilizerCategory category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));
        FertilizerCompany company = companyRepository.findById(dto.getCompanyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));
        stock.setUser(user);
        stock.setCategory(category);
        stock.setCompany(company);
        stock.setWeightPerQuantity(dto.getWeightPerQuantity());
        stock.setPurchasePrice(dto.getPurchasePrice());
        stock.setSellPrice(dto.getSellPrice());
        stock.setWarehouse(dto.getWarehouse());
        stock.setQuantity(dto.getQuantity());
        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStock(Long id) {
        FertilizerStock stock = stockRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fertilizer stock not found: " + id));
        stockRepository.delete(stock);
    }

    @Transactional(readOnly = true)
    public List<FertilizerStockDisplayDTO> getStocksForSupplier(Long supplierId) {
        List<FertilizerStock> stocks = stockRepository.findByUserId(supplierId);
        return stocks.stream().map(stock -> {
            String productName = stock.getCompany().getName() + " " + stock.getCategory().getName();
            return new FertilizerStockDisplayDTO(
                stock.getId(),
                productName,
                stock.getWeightPerQuantity(),
                stock.getSellPrice()
            );
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FertilizerStockDisplayDTO> getStocksDisplay(Long supplierId) {
        List<FertilizerStock> stocks = (supplierId == null)
            ? stockRepository.findAll()
            : stockRepository.findByUserId(supplierId);
        return stocks.stream().map(stock -> {
            String productName = stock.getCompany().getName() + " " + stock.getCategory().getName();
            return new FertilizerStockDisplayDTO(
                stock.getId(),
                productName,
                stock.getWeightPerQuantity(),
                stock.getSellPrice()
            );
        }).collect(Collectors.toList());
    }
}
