package com.parkit.parkingsystem.unittests.dao;

import com.parkit.parkingsystem.constants.CustomMessages;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.helper.DataBasePrepareService;
import com.parkit.parkingsystem.model.DataBaseConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.DataBaseConfigService;
import com.parkit.parkingsystem.service.DataBaseService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    //TODO : idem que pour prod (move to resources)
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

    @Test
    public void should_LogErrorOnSQLException_whenGetNextAvailableSlotAndGetConnectionWithWrongDatabaseConfig() {
        DataBaseService dataBaseServiceFake = new DataBaseService(new DataBaseConfig("x", "y", "z"));
        ParkingSpotDAO parkingSpotDAOFake = new ParkingSpotDAO(dataBaseServiceFake);
        LogCaptor logCaptor = LogCaptor.forClass(ParkingSpotDAO.class);

        parkingSpotDAOFake.getNextAvailableSlot(ParkingType.CAR);

        assertThat(logCaptor.getErrorLogs()).containsExactly(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR);
    }

    @Test
    public void should_LogErrorOnException_whenGetNextAvailableSlotAndGetConnectionWithNullDatabaseConfig() {
        DataBaseService dataBaseServiceFake = new DataBaseService(null);
        ParkingSpotDAO parkingSpotDAOFake = new ParkingSpotDAO(dataBaseServiceFake);
        LogCaptor logCaptor = LogCaptor.forClass(ParkingSpotDAO.class);

        parkingSpotDAOFake.getNextAvailableSlot(ParkingType.CAR);

        assertThat(logCaptor.getErrorLogs()).containsExactly(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR);
    }

    @Test
    public void should_LogErrorOnSQLException_whenUpdateParkingAndGetConnectionWithWrongDatabaseConfig() {
        DataBaseService dataBaseServiceFake = new DataBaseService(new DataBaseConfig("x", "y", "z"));
        ParkingSpotDAO parkingSpotDAOFake = new ParkingSpotDAO(dataBaseServiceFake);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        LogCaptor logCaptor = LogCaptor.forClass(ParkingSpotDAO.class);

        parkingSpotDAOFake.updateParking(parkingSpot);

        assertThat(logCaptor.getErrorLogs()).containsExactly(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR);
    }

    @Test
    public void should_LogErrorOnException_whenUpdateParkingAndGetConnectionWithNullDatabaseConfig() {
        DataBaseService dataBaseServiceFake = new DataBaseService(null);
        ParkingSpotDAO parkingSpotDAOFake = new ParkingSpotDAO(dataBaseServiceFake);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        LogCaptor logCaptor = LogCaptor.forClass(ParkingSpotDAO.class);

        parkingSpotDAOFake.updateParking(parkingSpot);

        assertThat(logCaptor.getErrorLogs()).containsExactly(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR);
    }
}
