package com.teafactory.pureleaf.inventoryProcess.entity;

import com.teafactory.pureleaf.routes.entity.Route;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@IdClass(Bag.BagId.class)
@Data
public class Bag {
    @Id
    private String bagNumber; // e.g., "001", "002", "003"

    @Id
    @Column(name = "route_id")
    private Long routeId;

    @ManyToOne
    @JoinColumn(name = "route_id", insertable = false, updatable = false)
    private Route route;

    private String status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BagId implements Serializable {
        private String bagNumber;
        private Long routeId;
    }
}
