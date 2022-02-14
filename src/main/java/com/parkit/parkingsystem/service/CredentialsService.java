package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CredentialsService {

    private static final Logger LOGGER = LogManager.getLogger("CredentialsService");

    public Credentials getCredentials() throws IOException {

        FileInputStream fileInputStream = null;
        Properties credentialsProperties = new Properties();
        Credentials credentials;
        try {
            fileInputStream = new FileInputStream(".\\databaseResources\\databaseCredentials.properties");
            credentialsProperties.load(fileInputStream);
            credentials = new Credentials(credentialsProperties.getProperty("datasourceUsername"), credentialsProperties.getProperty("datasourcePassword"));
        } catch (Exception e) {
            LOGGER.error("properties read failed", e);
            throw e;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return credentials;

    }
}
