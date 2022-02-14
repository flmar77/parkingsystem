package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {
    private final int number;
    private final ParkingType parkingType;
    private boolean isAvailable;

    public ParkingSpot(final int number, final ParkingType parkingType, final boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return number;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(final boolean available) {
        isAvailable = available;
    }

}
