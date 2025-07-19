package com.teafactory.pureleaf.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.teafactory.pureleaf.entity.User;

@Getter
@Setter
public class SupplierDTO {
    private User user;
    private Long factoryId;
    private Long routeId;
    private Double landSize;
    private String landLocation;
    private String pickupLocation;
    private String nicImage;
    private LocalDate approvedDate;
    private Boolean isActive;
}
