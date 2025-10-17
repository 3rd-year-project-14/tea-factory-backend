package com.teafactory.pureleaf.fertilizer.entity;

import com.teafactory.pureleaf.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private FertilizerCategory category;

    @ManyToOne(optional = false)
    private FertilizerCompany company;

    @Column(nullable = false)
    private Double weightPerQuantity;

    @Column(nullable = false)
    private Double purchasePrice;

    @Column(nullable = false)
    private Double sellPrice;

    @Column(nullable = false)
    private String warehouse;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new java.util.Date();
        }
    }
}
