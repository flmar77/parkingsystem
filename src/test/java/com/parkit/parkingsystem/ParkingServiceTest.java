package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    public void setUpEachTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void should_updateParkingSpot_WhenExitingVehicle() {
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void should_saveTicket_WhenProcessIncomingVehicle() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void should_searchVehicleRegNumber_WhenProcessIncomingVehicle() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(ticketDAO.searchVehicleRegNumber("ABCDEF")).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).searchVehicleRegNumber(anyString());
    }

    @Test
    public void should_saveTicketWithDiscount_WhenProcessIncomingVehicleAndRecurringUsers() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        when(ticketDAO.searchVehicleRegNumber(anyString())).thenReturn(true);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).saveTicket(argThat(ticket -> {
            assertThat(ticket.getDiscount()).isTrue();
            return true;
        }));
    }
}