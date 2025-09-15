package com.teafactory.pureleaf.fertilizer.service;

import com.teafactory.pureleaf.fertilizer.dto.StockDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import com.teafactory.pureleaf.fertilizer.entity.Stock;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCompanyRepository;
import com.teafactory.pureleaf.fertilizer.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private FertilizerCategoryRepository fertilizerCategoryRepository;
    @Autowired
    private FertilizerCompanyRepository fertilizerCompanyRepository;

    public List<StockDTO> getAllStocks() {
        return stockRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public StockDTO addStock(StockDTO dto) {
        Stock stock = toEntity(dto);
        // Set companyName and categoryName from related entities
        if (stock.getCompany() != null) {
            stock.setCompanyName(stock.getCompany().getName());
        }
        if (stock.getCategory() != null) {
            stock.setCategoryName(stock.getCategory().getName());
        }
        Stock saved = stockRepository.save(stock);
        return toDTO(saved);
    }

    public StockDTO getStockById(Long id) {
        return stockRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public StockDTO updateStock(Long id, StockDTO dto) {
        return stockRepository.findById(id).map(existingStock -> {
            FertilizerCategory category = fertilizerCategoryRepository.findById(dto.getCategoryId()).orElse(null);
            FertilizerCompany company = fertilizerCompanyRepository.findById(dto.getCompanyId()).orElse(null);
            existingStock.setCategory(category);
            existingStock.setCompany(company);
            // Set companyName and categoryName from related entities
            existingStock.setCompanyName(company != null ? company.getName() : null);
            existingStock.setCategoryName(category != null ? category.getName() : null);
            existingStock.setWeight(dto.getWeight());
            existingStock.setQuantity(dto.getQuantity());
            existingStock.setWarehouseName(dto.getWarehouseName());
            existingStock.setManufactureDate(dto.getManufactureDate());
            existingStock.setExpiryDate(dto.getExpiryDate());
            Stock updated = stockRepository.save(existingStock);
            return toDTO(updated);
        }).orElse(null);
    }

    // Mapping methods
    private StockDTO toDTO(Stock stock) {
        StockDTO dto = new StockDTO();
        dto.setId(stock.getId());
        dto.setCategoryId(stock.getCategory() != null ? stock.getCategory().getId() : null);
        dto.setCategoryName(stock.getCategory() != null ? stock.getCategory().getName() : null);
        dto.setCompanyId(stock.getCompany() != null ? stock.getCompany().getId() : null);
        dto.setCompanyName(stock.getCompany() != null ? stock.getCompany().getName() : null);
        dto.setWeight(stock.getWeight());
        dto.setQuantity(stock.getQuantity());
        dto.setWarehouseName(stock.getWarehouseName());
        dto.setManufactureDate(stock.getManufactureDate());
        dto.setExpiryDate(stock.getExpiryDate());
        return dto;
    }

    private Stock toEntity(StockDTO dto) {
        Stock stock = new Stock();
        stock.setId(dto.getId());
        FertilizerCategory category = fertilizerCategoryRepository.findById(dto.getCategoryId()).orElse(null);
        FertilizerCompany company = fertilizerCompanyRepository.findById(dto.getCompanyId()).orElse(null);
        stock.setCategory(category);
        stock.setCompany(company);
        // Set companyName and categoryName from related entities
        stock.setCompanyName(company != null ? company.getName() : null);
        stock.setCategoryName(category != null ? category.getName() : null);
        stock.setWeight(dto.getWeight());
        stock.setQuantity(dto.getQuantity());
        stock.setWarehouseName(dto.getWarehouseName());
        stock.setManufactureDate(dto.getManufactureDate());
        stock.setExpiryDate(dto.getExpiryDate());
        return stock;
    }
}