package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DataBaseConfigService {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfigService");

    private final String databaseConfigPath;

    public DataBaseConfigService(String databaseConfigPath) {
        this.databaseConfigPath = databaseConfigPath;
    }

    public DataBaseConfig getDataBaseConfig() {

        FileInputStream fileInputStream = null;
        Properties dataBaseProperties = new Properties();
        DataBaseConfig dataBaseConfig = null;
        try {
            fileInputStream = new FileInputStream(databaseConfigPath);
            dataBaseProperties.load(fileInputStream);
            dataBaseConfig = new DataBaseConfig(
                    dataBaseProperties.getProperty("dataBaseUrl"),
                    dataBaseProperties.getProperty("dataBaseUsername"),
                    dataBaseProperties.getProperty("dataBasePassword")
            );
        } catch (FileNotFoundException e) {
            LOGGER.error("dataBaseConfig file properties not found : " + databaseConfigPath, e);
        } catch (IOException e) {
            LOGGER.error("error while reading dataBaseConfig file properties: " + databaseConfigPath, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("dataBaseConfig file properties contains malformed Unicode escape sequence: " + databaseConfigPath, e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error("error while closing dataBaseConfig file properties: " + databaseConfigPath, e);
                }
            }
        }
        return dataBaseConfig;
    }
}
