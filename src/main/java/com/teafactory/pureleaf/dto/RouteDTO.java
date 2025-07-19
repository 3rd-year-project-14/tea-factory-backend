package com.teafactory.pureleaf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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

