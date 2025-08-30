package com.teafactory.pureleaf.entity;

import com.teafactory.pureleaf.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tea_supply_request")
public class TeaSupplyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private LocalDate supplyDate;
    private Integer estimatedBagCount;
    private String status;

    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
