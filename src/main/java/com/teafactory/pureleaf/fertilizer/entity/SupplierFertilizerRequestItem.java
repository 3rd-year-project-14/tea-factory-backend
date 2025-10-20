package com.teafactory.pureleaf.fertilizer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplier_fertilizer_request_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierFertilizerRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_fertilizer_request_id", nullable = false)
    private SupplierFertilizerRequest supplierFertilizerRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fertilizer_stock_id", nullable = false)
    private FertilizerStock fertilizerStock;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String rejectReason;
}

