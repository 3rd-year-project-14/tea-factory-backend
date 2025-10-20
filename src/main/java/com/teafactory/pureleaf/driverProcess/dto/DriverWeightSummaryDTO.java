package com.teafactory.pureleaf.driverProcess.dto;

public class DriverWeightSummaryDTO {
    private double totalNetWeight;
    private double totalGrossWeight;
    private double totalWater;
    private double totalCoarse;
    private double totalTareWeight;
    private int totalBagCount;

    public DriverWeightSummaryDTO() {}

    public DriverWeightSummaryDTO(double totalNetWeight, double totalGrossWeight, double totalWater, double totalCoarse, double totalTareWeight, int totalBagCount) {
        this.totalNetWeight = totalNetWeight;
        this.totalGrossWeight = totalGrossWeight;
        this.totalWater = totalWater;
        this.totalCoarse = totalCoarse;
        this.totalTareWeight = totalTareWeight;
        this.totalBagCount = totalBagCount;
    }

    public double getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(double totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public double getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(double totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public double getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(double totalWater) {
        this.totalWater = totalWater;
    }

    public double getTotalCoarse() {
        return totalCoarse;
    }

    public void setTotalCoarse(double totalCoarse) {
        this.totalCoarse = totalCoarse;
    }

    public double getTotalTareWeight() {
        return totalTareWeight;
    }

    public void setTotalTareWeight(double totalTareWeight) {
        this.totalTareWeight = totalTareWeight;
    }

    public int getTotalBagCount() {
        return totalBagCount;
    }

    public void setTotalBagCount(int totalBagCount) {
        this.totalBagCount = totalBagCount;
    }
}

