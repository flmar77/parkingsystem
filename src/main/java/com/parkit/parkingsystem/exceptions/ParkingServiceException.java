package com.parkit.parkingsystem.exceptions;

public class ParkingServiceException extends Exception {

    public ParkingServiceException(String message) {
        super(message);
    }

    public ParkingServiceException(String message, Exception e) {
        super(message, e);
    }

}
