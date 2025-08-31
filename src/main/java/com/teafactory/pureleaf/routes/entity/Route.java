package com.teafactory.pureleaf.routes.entity;

import com.teafactory.pureleaf.entity.Driver;
import com.teafactory.pureleaf.entity.Factory;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_id")
    private Factory factory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "route_code", unique = true)
    private String routeCode;

    private String name;

    private String startLocation;

    private String endLocation;

    private Integer bagCount;

    private Double distance;

    private Integer supplierCount;

    private Boolean status;

    private LocalDateTime createdAt;
}
