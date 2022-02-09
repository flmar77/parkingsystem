package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

import static com.parkit.parkingsystem.util.Rounder.round;

public class FareCalculatorService {

    public void calculateFare(final Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        long parkingTimeInMillis = outTime.getTime() - inTime.getTime();
        double parkingTimeInHours = (double) parkingTimeInMillis / Fare.MILLIS_PER_HOUR;
        parkingTimeInHours = round(parkingTimeInHours, 2);

        if (parkingTimeInHours < Fare.FREE_TIME_IN_HOUR) {
            ticket.setPrice(0);
            return;
        }
        double discountAppliedRatio = 1;
        if (ticket.getDiscount()) {
            discountAppliedRatio = Fare.DISCOUNT_DEFAULT_RATIO;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(parkingTimeInHours * Fare.CAR_RATE_PER_HOUR * discountAppliedRatio);
                break;
            }
            case BIKE: {
                ticket.setPrice(parkingTimeInHours * Fare.BIKE_RATE_PER_HOUR * discountAppliedRatio);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
