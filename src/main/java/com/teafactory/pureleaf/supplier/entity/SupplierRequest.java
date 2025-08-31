package com.teafactory.pureleaf.supplier.entity;


import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.ConnectionBuilder;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "supplier_request")
public class SupplierRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_id")
    private Factory factory;

    @Column(name = "status")
    private String status;

    @Column(name = "land_size")
    private Double landSize;

    @Column(name = "monthly_supply")
    private Double monthlySupply;

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

}

