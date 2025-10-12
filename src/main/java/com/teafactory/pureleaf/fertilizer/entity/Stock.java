package com.teafactory.pureleaf.fertilizer.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private FertilizerCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private FertilizerCompany company;

    private String companyName; // Store company name
    private String categoryName; // Store category name
    private Double weight; // Weight per unit
    private Integer quantity;
    private String warehouseName;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
}