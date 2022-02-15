package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBasePrepareService {

    private static final Logger LOGGER = LogManager.getLogger("DataBasePrepareService");

    private final DataBaseTestConfig dataBaseTestConfig;

    public DataBasePrepareService(DataBaseTestConfig dataBaseTestConfig) {
        this.dataBaseTestConfig = dataBaseTestConfig;
    }

    public void clearDataBaseEntries() {

        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement(DBConstants.CLEAN_TEST_PARKING).execute();

            //clear ticket entries;
            connection.prepareStatement(DBConstants.CLEAN_TEST_TICKET).execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataBaseTestConfig.closeConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("test database clean failed");
            }

        }
    }

    public void setAllParkingSpotNotAvailable() {

        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to not available
            connection.prepareStatement(DBConstants.SET_PARKING_NOT_AVAILABLE).execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dataBaseTestConfig.closeConnection(connection);
            } catch (SQLException e) {
                LOGGER.error("test database set all parking spots not available failed");
            }
        }
    }


}
