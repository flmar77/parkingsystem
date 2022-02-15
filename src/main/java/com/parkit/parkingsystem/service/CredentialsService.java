package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CredentialsService {

    private static final Logger LOGGER = LogManager.getLogger("CredentialsService");

    private static final String filepath = ".\\databaseResources\\databaseCredentials.properties";

    public Credentials getCredentials() {

        FileInputStream fileInputStream = null;
        Properties credentialsProperties = new Properties();
        Credentials credentials = null;
        try {
            fileInputStream = new FileInputStream(filepath);
            credentialsProperties.load(fileInputStream);
            credentials = new Credentials(credentialsProperties.getProperty("datasourceUsername"), credentialsProperties.getProperty("datasourcePassword"));
        } catch (FileNotFoundException e) {
            LOGGER.error("credentials file properties not found : " + filepath, e);
        } catch (IOException e) {
            LOGGER.error("error while reading credentials file properties: " + filepath, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("credentials file properties contains malformed Unicode escape sequence: " + filepath, e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error("error while closing credentials file properties: " + filepath, e);
                }
            }
        }
        return credentials;
    }
}
