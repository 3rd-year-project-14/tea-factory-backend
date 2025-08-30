package com.teafactory.pureleaf.supplier.entity;


import java.time.LocalDate;

import com.teafactory.pureleaf.entity.Factory;
import com.teafactory.pureleaf.entity.Route;
import com.teafactory.pureleaf.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "factory_id")
    private Factory factory;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    private Double landSize;

    private String landLocation;

    private String pickupLocation;

    private String nicImage;

    private LocalDate approvedDate;

    private Boolean isActive;

    private Long supplierRequestId;

    @Column(name = "initial_bag_count")
    private Integer initialBagCount;

    @Column(name = "pickup_to_route_start_distance")
    private Double pickupToRouteStartDistance;
}


