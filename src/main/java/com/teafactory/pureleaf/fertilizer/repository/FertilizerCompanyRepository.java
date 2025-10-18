package com.teafactory.pureleaf.fertilizer.repository;

import com.teafactory.pureleaf.fertilizer.entity.FertilizerCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FertilizerCompanyRepository extends JpaRepository<FertilizerCompany, Long> {
    Optional<FertilizerCompany> findByNameIgnoreCase(String name);

    // Fetch company with categories for dropdown dependent loading
    @Query("select fc from FertilizerCompany fc left join fetch fc.categories where fc.id = :id")
    Optional<FertilizerCompany> findWithCategoriesById(@Param("id") Long id);

    // Fixed: Remove ORDER BY from DISTINCT query to avoid PostgreSQL error
    @Query("select distinct fc from FertilizerCompany fc join fc.categories c where c.id = :categoryId")
    List<FertilizerCompany> findAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("select distinct fc from FertilizerCompany fc join fc.categories c where c.id in :categoryIds order by lower(fc.name)")
    List<FertilizerCompany> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}
