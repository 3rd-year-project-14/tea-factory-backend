package com.teafactory.pureleaf.fertilizer.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fertilizer_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore // Prevent infinite recursion during JSON serialization
    @ManyToMany(mappedBy = "categories")
    private Set<FertilizerCompany> companies = new HashSet<>();
}
