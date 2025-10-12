package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import org.springframework.data.jpa.repository.JpaRepository;
//
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FertilizerCompanyRepository extends JpaRepository<FertilizerCompany, Long> {
    Optional<FertilizerCompany> findByNameIgnoreCase(String name);

    // Fetch company with categories for dropdown dependent loading
    @Query("select fc from FertilizerCompany fc left join fetch fc.categories where fc.id = :id")
    Optional<FertilizerCompany> findWithCategoriesById(@Param("id") Long id);
}
