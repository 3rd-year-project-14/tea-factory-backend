package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.CreateFertilizerStockDTO;
import com.teafactory.pureleaf.fertilizer.dto.FertilizerStockDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerStock;
import com.teafactory.pureleaf.fertilizer.service.FertilizerStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-stocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FertilizerStockController {
    private final FertilizerStockService fertilizerStockService;

    @PostMapping
    public ResponseEntity<FertilizerStock> addStock(@Valid @RequestBody CreateFertilizerStockDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + authentication.getAuthorities());
        FertilizerStock stock = fertilizerStockService.addStock(dto);
        return new ResponseEntity<>(stock, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FertilizerStockDTO>> getAllStocks() {
        return ResponseEntity.ok(fertilizerStockService.getAllStocks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FertilizerStock> updateStock(@PathVariable Long id, @Valid @RequestBody CreateFertilizerStockDTO dto) {
        FertilizerStock updated = fertilizerStockService.updateStock(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        fertilizerStockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
