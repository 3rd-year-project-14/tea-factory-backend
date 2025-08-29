package com.teafactory.pureleaf.supplier.entity;


import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "supplier_request")
@Getter
@Setter
public class SupplierRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'pending'")
    private String status = "pending";

    @Column(name = "land_size")
    private Double landSize;

    @Column(name = "monthly_supply")
    private Double monthlySupply;

    @Column(name = "requested_route")
    private String requestedRoute;

    @Column(name = "nic_image")
    private String nicImage;

    @Column( name="requested_date")
    private LocalDate requestedDate;

    @Column(name = "rejected_date")
    private LocalDate rejectedDate;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "land_location")
    private String landLocation;

    @ManyToOne
    @JoinColumn(name = "factory_id")
    private Factory factory;
}

