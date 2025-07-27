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
    @JoinColumn(name = "supply_request_id", nullable = false)
    private TeaSupplyRequest teaSupplyRequest;

    private LocalTime arrivedTime;
    private LocalTime completedTime;
    private String status;
    private java.time.LocalDate date;

    public static class TripSupplierId implements Serializable {
        private Long trip;
        private Long teaSupplyRequest;
        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TripSupplierId that = (TripSupplierId) o;
            return trip.equals(that.trip) && teaSupplyRequest.equals(that.teaSupplyRequest);
        }
        @Override
        public int hashCode() {
            return java.util.Objects.hash(trip, teaSupplyRequest);
        }
        public TripSupplierId() {}
        public TripSupplierId(Long trip, Long teaSupplyRequest) {
            this.trip = trip;
            this.teaSupplyRequest = teaSupplyRequest;
        }
    }
}
