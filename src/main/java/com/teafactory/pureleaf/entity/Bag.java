package com.teafactory.pureleaf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "bag")
@IdClass(Bag.BagId.class)
public class Bag {
    @Id
    private Long bagId;

    @Id
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    public static class BagId implements Serializable {
        private Long bagId;
        private Long route;
        public BagId() {}
        public BagId(Long bagId, Long route) {
            this.bagId = bagId;
            this.route = route;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BagId that = (BagId) o;
            return bagId.equals(that.bagId) && route.equals(that.route);
        }
        @Override
        public int hashCode() {
            return java.util.Objects.hash(bagId, route);
        }
    }
}
