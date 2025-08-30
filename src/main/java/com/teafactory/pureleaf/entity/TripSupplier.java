package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "trip_supplier")
public class TripSupplier {
    @EmbeddedId
    private TripSupplierId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tripId")
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requestId")
    @JoinColumn(name = "supply_request_id")
    private TeaSupplyRequest teaSupplyRequest;

    private LocalTime arrivalTime;
    private LocalTime completionTime;

    private String status;
}
