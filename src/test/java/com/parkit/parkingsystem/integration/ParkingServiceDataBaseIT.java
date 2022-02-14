package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Credentials;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.CredentialsService;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("NewClassNamingConvention")
@ExtendWith(MockitoExtension.class)
public class ParkingServiceDataBaseIT {

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;

    @Spy
    private static TicketDAO ticketDAO;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws IOException {
        CredentialsService credentialsService = new CredentialsService();
        Credentials credentials = credentialsService.getCredentials();
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig(credentials);
        parkingSpotDAO = new ParkingSpotDAO(dataBaseTestConfig);
        ticketDAO = new TicketDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestConfig);
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void should_returnSecondParkingSpotAndTicket_whenFirstParkingSpotBookedAndTicketCreated() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);

        parkingService.processIncomingVehicle();

        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
        assertThat(ticketDAO.getCurrentTicket("ABCDEF")).isNotNull();
    }

    @Test
    public void should_calculateFareAndPopulateOutTime_whenProcessExitingVehicle() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).updateTicket(argThat(t -> {
            assertThat(t.getPrice()).isNotEqualTo(0);
            return true;
        }));
        assertThat(ticketDAO.getCurrentTicket("ABCDEF")).isNull();
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
    }

    @Test
    public void should_updateCurrentTicket_whenProcessExitingVehicleAndRecurringUsers() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(System.currentTimeMillis() - (120 * 60 * 1000)));
        ticket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);
        ticket.setPrice(0);
        ticket.setInTime(new Date(System.currentTimeMillis() - (45 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setDiscount(true);
        ticketDAO.saveTicket(ticket);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).updateTicket(argThat(t -> {
            assertThat(t.getPrice()).isNotEqualTo(0);
            return true;
        }));
        assertThat(ticketDAO.getCurrentTicket("ABCDEF")).isNull();
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
    }

}
