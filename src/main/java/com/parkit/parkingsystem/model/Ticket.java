package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discount;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(final ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(final String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime == null ? null : new Date(inTime.getTime());
    }

    public void setInTime(final Date inTime) {
        this.inTime = inTime == null ? null : new Date(inTime.getTime());
    }

    public Date getOutTime() {
        return outTime == null ? null : new Date(outTime.getTime());
    }

    public void setOutTime(final Date outTime) {
        this.outTime = outTime == null ? null : new Date(outTime.getTime());
    }

    public boolean getDiscount() {
        return discount;
    }

    public void setDiscount(final boolean discount) {
        this.discount = discount;
    }
}
