package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void should_returnFirstSpot_whenGetNextAvailableSlot() throws Exception {
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void should_return0_whenNoAvailableSlot() throws Exception {
        dataBasePrepareService.setAllParkingSpotNotAvailable();

        assertEquals(0, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void should_returnTrue_whenUpdateParkingFromAvailableToNotAvailable() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void should_returnFalse_whenUpdateParkingOfNonExistentParkingNumber() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(6, ParkingType.CAR, true);

        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
}
