// OOP Concept: Package (Encapsulation, Organization)
package com.teafactory.pureleaf.fertilizer.entity;

// OOP Concept: Importing Classes (Abstraction, Reusability)
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

// OOP Concept: Class Declaration (Encapsulation, Abstraction)
@Entity
@Table(name = "fertilizer_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerCategory {
    // OOP Concept: Field Declaration (Encapsulation)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OOP Concept: Field Declaration (Encapsulation)
    @Column(unique = true)
    private String name;

    // OOP Concept: Association (Encapsulation, Abstraction)
    @JsonIgnore // Prevent infinite recursion during JSON serialization
    @ManyToMany(mappedBy = "categories")
    private Set<FertilizerCompany> companies = new HashSet<>();
}
