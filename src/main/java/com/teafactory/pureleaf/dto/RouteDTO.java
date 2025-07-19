package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long routeId;
    private String name;
    private String startLocation;
    private String endLocation;
    private Integer bagCount;
    private Double distance;
    private Integer supplierCount;
    private String status;
    private LocalDateTime createdAt;
    private Long factoryId;
}
