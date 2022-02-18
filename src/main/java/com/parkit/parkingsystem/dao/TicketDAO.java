package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.CustomMessages;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.DataBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TicketDAO {

    private static final Logger LOGGER = LogManager.getLogger(TicketDAO.class);

    private final DataBaseService dataBaseService;

    public TicketDAO(final DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    /**
     * @param ticket we want to save
     * @return true if successfully, else false
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, DISCOUNT)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
            ps.setBoolean(6, ticket.getDiscount());
            result = (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            dataBaseService.closePreparedStatement(ps);
            dataBaseService.closeConnection(con);
        }
        return result;
    }

    /**
     * @param vehicleRegNumber we want to get current ticket
     * @return current ticket if successfully, else null
     */
    public Ticket getCurrentTicket(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ticket ticket = null;
        try {
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.GET_CURRENT_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, DISCOUNT)
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(7)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
                ticket.setDiscount(rs.getBoolean(6));
            }
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            dataBaseService.closeResultSet(rs);
            dataBaseService.closePreparedStatement(ps);
            dataBaseService.closeConnection(con);
        }
        return ticket;
    }

    /**
     * @param ticket we want to update
     * @return true if successfully, else false
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            result = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            dataBaseService.closePreparedStatement(ps);
            dataBaseService.closeConnection(con);
        }
        return result;
    }

    /**
     * @param vehicleRegNumber we want to search to know if recurring user or not
     * @return true if recurring user, else false
     */
    public boolean searchVehicleRegNumber(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = dataBaseService.getConnection();
            ps = con.prepareStatement(DBConstants.SEARCH_VEHICLE_REG_NUMBER);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_EXECUTE_ERROR, e);
        } catch (Exception e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_DATABASE_CONNECTION_ERROR, e);
        } finally {
            dataBaseService.closeResultSet(rs);
            dataBaseService.closePreparedStatement(ps);
            dataBaseService.closeConnection(con);
        }
        return result;
    }
}
