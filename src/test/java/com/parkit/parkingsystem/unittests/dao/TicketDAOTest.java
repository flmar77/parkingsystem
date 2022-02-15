package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.CredentialsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketDAOTest {

    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        CredentialsService credentialsService = new CredentialsService();
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig(credentialsService);
        ticketDAO = new TicketDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestConfig);
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void should_returnTrue_whenSaveValidTicket() {
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
    public void should_returnFalse_whenSaveNullTicket() {
        Ticket ticket = new Ticket();

        assertFalse(ticketDAO.saveTicket(ticket));

    }

    @Test
    public void should_returnTicket_whenGetCurrentTicket() {
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
    public void should_returnNull_whenGetNoCurrentTicket() {
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

        assertThat(ticketDAO.getCurrentTicket("ABCDEF")).isNull();
    }

    @Test
    public void should_returnTrue_whenUpdateExistentTicket() {
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
    public void should_returnFalse_whenUpdateNonExistentTicket() {
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
    public void should_returnTrue_whenSearchVehicleRegNumberOfRecurringUser() {
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
    public void should_returnFalse_whenSearchVehicleRegNumberOfNoRecurringUser() {

        assertThat(ticketDAO.searchVehicleRegNumber("ABCDEF")).isFalse();
    }
}
