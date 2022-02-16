package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseService {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseService");

    private final DataBaseConfig dataBaseConfig;

    public DataBaseService(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public Connection getConnection() throws Exception {
        if (dataBaseConfig == null) {
            throw new Exception("can't connect to database : error while getting dataBaseConfig");
        }
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                dataBaseConfig.getDataBaseUrl(), dataBaseConfig.getDataBaseUsername(), dataBaseConfig.getDataBasePassword());
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
