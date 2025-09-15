package com.teafactory.pureleaf.fertilizer.controller;


import com.teafactory.pureleaf.fertilizer.dto.StockDTO;
import com.teafactory.pureleaf.fertilizer.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    public List<StockDTO> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping
    public StockDTO addStock(@RequestBody StockDTO dto) {
        return stockService.addStock(dto);
    }

    @GetMapping("/{id}")
    public StockDTO getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }

    @PutMapping("/{id}")
    public StockDTO updateStock(@PathVariable Long id, @RequestBody StockDTO dto) {
        return stockService.updateStock(id, dto);
    }
}