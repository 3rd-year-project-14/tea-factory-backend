package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FertilizerCompanyRepository extends JpaRepository<FertilizerCompany, Long> {
    Optional<FertilizerCompany> findByNameIgnoreCase(String name);
}
