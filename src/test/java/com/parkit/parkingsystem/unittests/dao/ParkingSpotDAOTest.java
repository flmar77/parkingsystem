package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.DataBaseConfigService;
import com.parkit.parkingsystem.service.DataBaseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static final String DATABASE_CONFIG_FILEPATH = "src/main/java/com/parkit/parkingsystem/config/dataBaseConfigTest.properties";

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        DataBaseConfigService dataBaseTestConfigService = new DataBaseConfigService(DATABASE_CONFIG_FILEPATH);
        DataBaseService dataBaseTestService = new DataBaseService(dataBaseTestConfigService.getDataBaseConfig());
        parkingSpotDAO = new ParkingSpotDAO(dataBaseTestService);
        dataBasePrepareService = new DataBasePrepareService(dataBaseTestService);
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
