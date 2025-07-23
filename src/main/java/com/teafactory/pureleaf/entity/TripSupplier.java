package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "trip_supplier")
@IdClass(TripSupplier.TripSupplierId.class)
public class TripSupplier {
    @Id
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Id
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private LocalTime arrivedTime;
    private LocalTime completedTime;
    private String status;

    public static class TripSupplierId implements Serializable {
        private Long trip;
        private Long supplier;
        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TripSupplierId that = (TripSupplierId) o;
            return trip.equals(that.trip) && supplier.equals(that.supplier);
        }
        @Override
        public int hashCode() {
            return java.util.Objects.hash(trip, supplier);
        }
    }
}

