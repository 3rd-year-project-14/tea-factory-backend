package com.teafactory.pureleaf.inventoryProcess.dto;

public class WeighedBagWeightDetailsDTO {
    private Long id;
    private Integer bagTotal;
    private Integer coarse;
    private Double grossWeight;
    private Double tareWeight;
    private Long supplierId;
    private String supplierName;
    private String status;

    public WeighedBagWeightDetailsDTO() {}

    public WeighedBagWeightDetailsDTO(Long id, Integer bagTotal, Integer coarse, Double grossWeight, Double tareWeight, Long supplierId, String supplierName, String status) {
        this.id = id;
        this.bagTotal = bagTotal;
        this.coarse = coarse;
        this.grossWeight = grossWeight;
        this.tareWeight = tareWeight;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getBagTotal() { return bagTotal; }
    public void setBagTotal(Integer bagTotal) { this.bagTotal = bagTotal; }

    public Integer getCoarse() { return coarse; }
    public void setCoarse(Integer coarse) { this.coarse = coarse; }

    public Double getGrossWeight() { return grossWeight; }
    public void setGrossWeight(Double grossWeight) { this.grossWeight = grossWeight; }

    public Double getTareWeight() { return tareWeight; }
    public void setTareWeight(Double tareWeight) { this.tareWeight = tareWeight; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

