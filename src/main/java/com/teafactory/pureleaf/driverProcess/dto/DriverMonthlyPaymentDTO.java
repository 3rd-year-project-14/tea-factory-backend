package com.teafactory.pureleaf.driverProcess.dto;

public class DriverMonthlyPaymentDTO {
    private Long driverId;
    private int month;
    private int year;
    private double netWeight;
    private double payment;

    public DriverMonthlyPaymentDTO(Long driverId, int month, int year, double netWeight, double payment) {
        this.driverId = driverId;
        this.month = month;
        this.year = year;
        this.netWeight = netWeight;
        this.payment = payment;
    }

    public Long getDriverId() {
        return driverId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public double getPayment() {
        return payment;
    }
}

