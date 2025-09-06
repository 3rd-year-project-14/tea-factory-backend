package com.teafactory.pureleaf.inventoryProcess.dto;

public class SupplierRequestBagSummaryDTO {
    private Long supplyRequestId;
    private int totalBags;
    private double totalWeight;

    public SupplierRequestBagSummaryDTO(Long supplyRequestId, int totalBags, double totalWeight) {
        this.supplyRequestId = supplyRequestId;
        this.totalBags = totalBags;
        this.totalWeight = totalWeight;
    }

    public Long getSupplyRequestId() {
        return supplyRequestId;
    }

    public void setSupplyRequestId(Long supplyRequestId) {
        this.supplyRequestId = supplyRequestId;
    }

    public int getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(int totalBags) {
        this.totalBags = totalBags;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
}

