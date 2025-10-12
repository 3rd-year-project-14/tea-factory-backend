package com.teafactory.pureleaf.inventoryProcess.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trip_bag")
@Getter
@Setter
public class TripBag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign keys to TripSupplier (composite key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "trip_id", referencedColumnName = "trip_id"),
        @JoinColumn(name = "supply_request_id", referencedColumnName = "supply_request_id")
    })
    private TripSupplier tripSupplier;

    // Foreign keys to Bag (composite key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "route_id", referencedColumnName = "route_id"),
        @JoinColumn(name = "bag_number", referencedColumnName = "bagNumber")
    })
    private Bag bag;

    private Double driverWeight;
    private Boolean wet;
    private Boolean coarse;
    private String type;
    private String status = "pending";
}
