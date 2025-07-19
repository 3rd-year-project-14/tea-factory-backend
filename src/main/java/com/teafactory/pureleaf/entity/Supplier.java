package com.teafactory.pureleaf.entity;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long supplierId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Long factoryId;
    private Long routeId;
    private Double landSize;
    private String landLocation;
    private String pickupLocation;
    private String nicImage;
    private LocalDate approvedDate;
    private Boolean isActive;
    private Long supplierRequestId;
    private Integer initialBagCount;
}
