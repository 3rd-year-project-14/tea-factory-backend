// OOP Concept: Package (Encapsulation, Organization)
package com.teafactory.pureleaf.fertilizer.repository;

// OOP Concept: Importing Classes (Abstraction, Reusability)
import com.teafactory.pureleaf.fertilizer.entity.FertilizerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// OOP Concept: Interface Declaration (Abstraction)
public interface FertilizerCategoryRepository extends JpaRepository<FertilizerCategory, Long> {
    // OOP Concept: Inheritance (Polymorphism, Abstraction)
    // Extends JpaRepository to inherit CRUD operations

    // OOP Concept: Method Signature (Abstraction, Polymorphism)
    Optional<FertilizerCategory> findByNameIgnoreCase(String name);
}