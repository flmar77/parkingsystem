package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private static long currentTimeMillis;
    private static Date inTime;
    private static Date outTime;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
        currentTimeMillis = System.currentTimeMillis();
        inTime = new Date();
        outTime = new Date();
        outTime.setTime(currentTimeMillis);
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void should_calculateFareCar_whenOneHourParkingTime() throws Exception {
        inTime.setTime(currentTimeMillis - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void should_calculateFareBike_whenOneHourParkingTime() throws Exception {
        inTime.setTime(currentTimeMillis - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void should_throwException_whenCalculateFareWithUnknownType() {
        inTime.setTime(currentTimeMillis - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void should_throwException_whenCalculateFareBikeWithFutureInTime() {
        inTime.setTime(currentTimeMillis + (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void should_calculateFareBike_whenLessThanOneHourParkingTime() throws Exception {
        inTime.setTime(currentTimeMillis - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void should_calculateFareCar_whenLessThanOneHourParkingTime() throws Exception {

        inTime.setTime(currentTimeMillis - (45 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((1.13), ticket.getPrice());
    }

    @Test
    public void should_calculateFareCar_whenMoreThanADayParkingTime() throws Exception {
        inTime.setTime(currentTimeMillis - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void should_calculateFreeFare_whenLessThan30MinutesParkingTime() throws Exception {
        inTime.setTime(currentTimeMillis - (29 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void should_calculateDiscountFare_whenRecurringUsers() throws Exception {
        inTime.setTime(currentTimeMillis - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setDiscount(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void should_calculateRoundedFare() throws Exception {
        inTime.setTime(currentTimeMillis - (59 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0.98, ticket.getPrice());
    }

    @Test
    public void should_calculateRoundedFareWithDiscount() throws Exception {
        inTime.setTime(currentTimeMillis - (45 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setDiscount(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(1.07, ticket.getPrice());
    }
}
