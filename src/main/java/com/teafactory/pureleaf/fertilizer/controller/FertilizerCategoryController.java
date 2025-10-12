package com.teafactory.pureleaf.fertilizer.controller;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import com.teafactory.pureleaf.fertilizer.repository.FertilizerCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizer-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FertilizerCategoryController {
    private final FertilizerCategoryRepository categoryRepository;

    @GetMapping
    public List<FertilizerCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}

