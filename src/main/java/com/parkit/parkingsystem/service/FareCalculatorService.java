package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        /* TODO : fix to 2 decimals price*/
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        long parkingTimeInMillis = outTime.getTime() - inTime.getTime();
        double parkingTimeInHours = (double) parkingTimeInMillis / 3600000;
        parkingTimeInHours = round(parkingTimeInHours, 2);

        if (parkingTimeInHours < Fare.FREE_TIME_IN_HOUR) {
            ticket.setPrice(0);
        } else {
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

    private double round(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException("invalid decimals");

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}