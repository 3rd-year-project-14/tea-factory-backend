package com.teafactory.pureleaf.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailsDTO {
    private Long id;
    private String supplierName;
    private String nic;
    private String email;
    private String address;
    private String contactNo;
    private Double landSize;
    private String nicImage;
    private String pickupLocation;
    private String landLocation;
    private LocalDate approvedDate;
    private String routeName;
    private Double pickupToRouteStartDistance;
    private Integer initialBagCount;
}
