package com.parkit.parkingsystem.unittests.model;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    public void should_getAll_whenNewTicket() {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(currentTimeMillis));
        ticket.setOutTime(null);
        ticket.setDiscount(true);

        assertEquals(1, ticket.getId());
        assertEquals(parkingSpot, ticket.getParkingSpot());
        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
        assertEquals(0, ticket.getPrice());
        assertEquals(new Date(currentTimeMillis), ticket.getInTime());
        assertNull(ticket.getOutTime());
        assertTrue(ticket.getDiscount());
    }
}
