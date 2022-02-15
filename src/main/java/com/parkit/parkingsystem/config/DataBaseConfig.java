package com.parkit.parkingsystem.config;

import com.parkit.parkingsystem.model.Credentials;
import com.parkit.parkingsystem.service.CredentialsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    protected final CredentialsService credentialsService;

    public DataBaseConfig(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    public Connection getConnection() throws Exception {
        Credentials credentials = credentialsService.getCredentials();
        if (credentials == null) {
            throw new Exception("can't connect to database : error while getting credentials");
        }
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", credentials.getDatasourceUsername(), credentials.getDatasourcePassword());
    }

    public void closeConnection(final Connection con) throws SQLException {
        if (con != null) {
            con.close();
            LOGGER.info("Closing DB connection");
        }
    }

    public void closePreparedStatement(final PreparedStatement ps) throws SQLException {
        if (ps != null) {
            ps.close();
            LOGGER.info("Closing Prepared Statement");
        }
    }

    public void closeResultSet(final ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
            LOGGER.info("Closing Result Set");
        }
    }
}
