package com.teafactory.pureleaf.dto;

import java.util.List;

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

    public static class TeaSupplyRequestInfo {
        private Long requestId;
        private Integer estimatedBagCount;
        private Long supplierId;
        private String supplierName;

        public TeaSupplyRequestInfo(Long requestId, Integer estimatedBagCount, Long supplierId, String supplierName) {
            this.requestId = requestId;
            this.estimatedBagCount = estimatedBagCount;
            this.supplierId = supplierId;
            this.supplierName = supplierName;
        }

        public Long getRequestId() { return requestId; }
        public void setRequestId(Long requestId) { this.requestId = requestId; }
        public Integer getEstimatedBagCount() { return estimatedBagCount; }
        public void setEstimatedBagCount(Integer estimatedBagCount) { this.estimatedBagCount = estimatedBagCount; }
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    }
}
