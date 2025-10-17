package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.dto.CreateFertilizerStockDTO;
import com.teafactory.pureleaf.fertilizer.entity.FertilizerStock;
import com.teafactory.pureleaf.fertilizer.service.FertilizerStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
}
