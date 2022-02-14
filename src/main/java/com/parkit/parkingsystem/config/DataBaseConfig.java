package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.model.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    protected final Credentials credentials;

    public DataBaseConfig(Credentials credentials) {
        this.credentials = credentials;
    }

    public Connection getConnection() throws Exception {
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");

        // End
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", credentials.getDatasourceUsername(), credentials.getDatasourcePassword());
    }

    public void closeConnection(final Connection con) throws SQLException {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
                throw e;
            }
        }
    }

    public void closePreparedStatement(final PreparedStatement ps) throws SQLException {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
                throw e;
            }
        }
    }

    public void closeResultSet(final ResultSet rs) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
                throw e;
            }
        }
    }
}
