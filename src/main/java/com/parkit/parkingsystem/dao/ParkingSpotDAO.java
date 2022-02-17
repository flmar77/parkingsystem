package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.CustomMessages;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.DataBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpotDAO {

    private static final Logger LOGGER = LogManager.getLogger(ParkingSpotDAO.class);

    private final DataBaseService dataBaseService;

    public ParkingSpotDAO(final DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    public int getNextAvailableSlot(final ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try {
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            try {
                dataBaseService.closeResultSet(rs);
                dataBaseService.closePreparedStatement(ps);
                dataBaseService.closeConnection(con);
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
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.getIsAvailable());
            ps.setInt(2, parkingSpot.getId());
            result = (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            try {
                dataBaseService.closePreparedStatement(ps);
                dataBaseService.closeConnection(con);
            } catch (SQLException e) {
                LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CLOSE_ERROR, e);
            }
        }
        return result;
    }
}
