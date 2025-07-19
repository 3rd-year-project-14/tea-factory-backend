package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_request")
@Getter
@Setter
public class SupplierRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "land_size")
    private Double landSize;

    @Column(name = "monthly_supply")
    private Double monthlySupply;

    @Column(name = "requested_route")
    private String requestedRoute;

    @Column(name = "nic_image")
    private String nicImage;

    @Column(name = "rejected_date", columnDefinition = "timestamp without time zone")
    private LocalDateTime rejectedDate;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "land_location")
    private String landLocation;
}
