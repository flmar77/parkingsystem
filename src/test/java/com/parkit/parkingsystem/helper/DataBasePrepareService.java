package com.parkit.parkingsystem.helper;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.service.DataBaseService;

import java.sql.Connection;

public class DataBasePrepareService {

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
            dataBaseService.closeConnection(connection);
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
            dataBaseService.closeConnection(connection);
        }
    }


}
