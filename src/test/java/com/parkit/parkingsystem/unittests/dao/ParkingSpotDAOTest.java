package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Credentials;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.CredentialsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() throws IOException {
        CredentialsService credentialsService = new CredentialsService();
        Credentials credentials = credentialsService.getCredentials();
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig(credentials);
        parkingSpotDAO = new ParkingSpotDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestConfig);
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void should_returnFirstSpot_whenGetNextAvailableSlot() {
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void should_return0_whenNoAvailableSlot() {
        dataBasePrepareService.setAllParkingSpotNotAvailable();

        assertEquals(0, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void should_returnTrue_whenUpdateParkingFromAvailableToNotAvailable() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void should_returnFalse_whenUpdateParkingOfNonExistentParkingNumber() {
        ParkingSpot parkingSpot = new ParkingSpot(6, ParkingType.CAR, true);

        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
}
