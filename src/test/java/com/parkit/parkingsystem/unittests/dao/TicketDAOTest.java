package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Credentials;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.CredentialsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketDAOTest {

    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() throws IOException {
        CredentialsService credentialsService = new CredentialsService();
        Credentials credentials = credentialsService.getCredentials();
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig(credentials);
        ticketDAO = new TicketDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestConfig);
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void should_returnTrue_whenSaveValidTicket() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(currentTimeMillis));
        ticket.setOutTime(null);
        ticket.setDiscount(false);

        assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void should_throwException_whenSaveNullTicket() {
        Ticket ticket = new Ticket();

        assertThrows(Exception.class, () -> ticketDAO.saveTicket(ticket));

    }

    @Test
    public void should_returnTicket_whenGetCurrentTicket() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(currentTimeMillis - (60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);

        assertThat(ticketDAO.getCurrentTicket("ABCDEF")).isNotNull();
    }

    @Test
    public void should_throwException_whenGetNoCurrentTicket() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(currentTimeMillis - (60 * 60 * 1000)));
        ticket.setOutTime(new Date(currentTimeMillis));
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);

        assertThrows(Exception.class, () -> ticketDAO.getCurrentTicket("ABCDEF"));
    }

    @Test
    public void should_returnTrue_whenUpdateExistentTicket() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date(currentTimeMillis - (60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);
        ticket.setPrice(1.5);
        ticket.setOutTime(new Date(currentTimeMillis));

        assertThat(ticketDAO.updateTicket(ticket)).isTrue();
    }

    @Test
    public void should_returnFalse_whenUpdateNonExistentTicket() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(currentTimeMillis - (60 * 60 * 1000)));
        ticket.setOutTime(new Date(currentTimeMillis));
        ticket.setDiscount(false);

        assertThat(ticketDAO.updateTicket(ticket)).isFalse();
    }

    @Test
    public void should_returnTrue_whenSearchVehicleRegNumberOfRecurringUser() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(currentTimeMillis - (60 * 60 * 1000)));
        ticket.setOutTime(new Date(currentTimeMillis));
        ticket.setDiscount(false);
        ticketDAO.saveTicket(ticket);

        assertThat(ticketDAO.searchVehicleRegNumber("ABCDEF")).isTrue();
    }

    @Test
    public void should_returnFalse_whenSearchVehicleRegNumberOfNoRecurringUser() throws Exception {

        assertThat(ticketDAO.searchVehicleRegNumber("ABCDEF")).isFalse();
    }
}
