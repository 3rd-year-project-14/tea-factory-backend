package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Bag.BagId.class)
public class Bag {
    @Id
    private String bagNumber; // e.g., "001", "002", "003"

    @Id
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    private String status;

    public static class BagId implements Serializable {
        private String bagNumber;
        private Long route;
        public BagId() {}
        public BagId(String bagNumber, Long route) {
            this.bagNumber = bagNumber;
            this.route = route;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BagId that = (BagId) o;
            return bagNumber.equals(that.bagNumber) && route.equals(that.route);
        }
        @Override
        public int hashCode() {
            return java.util.Objects.hash(bagNumber, route);
        }
    }

    public String getBagNumber() {
        return bagNumber;
    }
    public void setBagNumber(String bagNumber) {
        this.bagNumber = bagNumber;
    }
    public Route getRoute() {
        return route;
    }
    public void setRoute(Route route) {
        this.route = route;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
