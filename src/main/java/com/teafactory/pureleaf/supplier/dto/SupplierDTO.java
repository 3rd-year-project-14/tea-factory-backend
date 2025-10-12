package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    private Long factoryId;
    private Long routeId;
    private Double landSize;
    private String landLocation;
    private String pickupLocation;
    private String nicImage;
    private LocalDate approvedDate;
    private Boolean isActive;
    private Long supplierId;
}