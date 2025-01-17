package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.exceptions.FareCalculatorException;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class FareCalculatorService {

    /**
     * Sets calculated fare price (rounded to 2 decimals places) into param ticket if successfully, else throws FareCalculatorException
     *
     * @param ticket we need to calculate fare
     * @throws FareCalculatorException when fare can't be calculated
     */

    public void calculateFare(final Ticket ticket) throws FareCalculatorException {

        if (ticket.getOutTime() == null) {
            throw new FareCalculatorException("Error while calculating fare - Out time is null");
        }
        if (ticket.getOutTime().before(ticket.getInTime())) {
            throw new FareCalculatorException("Error while calculating fare - Out time is before In Time : Out=" + ticket.getOutTime() + " / In=" + ticket.getInTime());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        BigDecimal parkingTimeInMillis = BigDecimal.valueOf(outTime.getTime() - inTime.getTime());
        BigDecimal millisPerHour = BigDecimal.valueOf(Fare.MILLIS_PER_HOUR);
        BigDecimal parkingTimeInHours = parkingTimeInMillis.divide(millisPerHour, 10, RoundingMode.HALF_UP);

        BigDecimal freeTimeInHour = BigDecimal.valueOf(Fare.FREE_TIME_IN_HOUR);

        if (parkingTimeInHours.compareTo(freeTimeInHour) < 0) {
            parkingTimeInHours = BigDecimal.valueOf(0);
        }

        BigDecimal discountAppliedRatio = BigDecimal.valueOf(1);
        if (ticket.getDiscount()) {
            discountAppliedRatio = BigDecimal.valueOf(Fare.DISCOUNT_DEFAULT_RATIO);
        }

        BigDecimal ratePerHour = BigDecimal.valueOf(0);
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                ratePerHour = BigDecimal.valueOf(Fare.CAR_RATE_PER_HOUR);
                break;
            case BIKE:
                ratePerHour = BigDecimal.valueOf(Fare.BIKE_RATE_PER_HOUR);
                break;
        }

        BigDecimal rawPrice = parkingTimeInHours.multiply(discountAppliedRatio).multiply(ratePerHour);
        BigDecimal RoundedPrice = rawPrice.setScale(2, RoundingMode.HALF_UP);
        ticket.setPrice(RoundedPrice.doubleValue());
    }
}
