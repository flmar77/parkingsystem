package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        long parkingTimeInMillis = outTime.getTime() - inTime.getTime();
        double parkingTimeInHours = (double)parkingTimeInMillis / 3600000;


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(parkingTimeInHours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(parkingTimeInHours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}