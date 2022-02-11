package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

public class DataBasePrepareService {

    final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

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
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
