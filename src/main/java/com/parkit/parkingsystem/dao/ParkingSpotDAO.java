package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.CustomMessages;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class ParkingSpotDAO {

    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");

    private final DataBaseConfig dataBaseConfig;

    public ParkingSpotDAO(final DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public int getNextAvailableSlot(final ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLTimeoutException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_TIMEOUT_ERROR, e);
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            try {
                dataBaseConfig.closeResultSet(rs);
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            } catch (SQLException e) {
                LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CLOSE_ERROR, e);
            }
        }
        return result;
    }

    public boolean updateParking(final ParkingSpot parkingSpot) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.getIsAvailable());
            ps.setInt(2, parkingSpot.getId());
            result = (ps.executeUpdate() == 1);
        } catch (SQLTimeoutException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_TIMEOUT_ERROR, e);
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            try {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            } catch (SQLException e) {
                LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CLOSE_ERROR, e);
            }
        }
        return result;
    }
}
