package com.parkit.parkingsystem.helper;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.service.DataBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBasePrepareService {

    private static final Logger LOGGER = LogManager.getLogger("DataBasePrepareService");

    private final DataBaseService dataBaseService;

    public DataBasePrepareService(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    public void clearDataBaseEntries() {

        Connection connection = null;
        try {
            connection = dataBaseService.getConnection();

            //set parking entries to available
            connection.prepareStatement(DBConstants.TEST_SET_PARKING_AVAILABLE).execute();

            //clear ticket entries;
            connection.prepareStatement(DBConstants.TEST_CLEAN_TICKET).execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataBaseService.closeConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("test database clean failed");
            }

        }
    }

    public void setAllParkingSpotNotAvailable() {

        Connection connection = null;
        try {
            connection = dataBaseService.getConnection();

            //set parking entries to not available
            connection.prepareStatement(DBConstants.TEST_SET_PARKING_NOT_AVAILABLE).execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataBaseService.closeConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("test database set all parking spots not available failed");
            }
        }
    }


}
