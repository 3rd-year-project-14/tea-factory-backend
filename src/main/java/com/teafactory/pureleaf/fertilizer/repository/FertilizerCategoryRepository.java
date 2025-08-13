package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FertilizerCategoryRepository extends JpaRepository<FertilizerCategory, Long> {
    Optional<FertilizerCategory> findByNameIgnoreCase(String name);
}
