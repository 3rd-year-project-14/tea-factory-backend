package com.teafactory.pureleaf.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public class TeaSupplyProcessResponseDTO {
    private List<TeaSupplyRequestInfo> requests;

    public TeaSupplyProcessResponseDTO() {}

    public TeaSupplyProcessResponseDTO(List<TeaSupplyRequestInfo> requests) {
        this.requests = requests;
    }

    public List<TeaSupplyRequestInfo> getRequests() {
        return requests;
    }

    public void setRequests(List<TeaSupplyRequestInfo> requests) {
        this.requests = requests;
    }

    @Data
    @AllArgsConstructor
    public static class TeaSupplyRequestInfo {
        private Long requestId;
        private Integer estimatedBagCount;
        private Long supplierId;
        private String supplierName;
        private Double pickupToRouteStartDistance;
        private String contactNo;
        private String pickupLocation;
        private String status;
    }
}
