package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseTestConfig");

    public DataBaseTestConfig(Credentials credentials) {
        super(credentials);
    }

    public Connection getConnection() throws Exception {
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", credentials.getDatasourceUsername(), credentials.getDatasourcePassword());
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
