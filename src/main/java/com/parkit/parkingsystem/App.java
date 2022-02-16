package com.parkit.parkingsystem;

import com.parkit.parkingsystem.exceptions.ParkingServiceException;
import com.parkit.parkingsystem.service.ParkingInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class App {

    private static final Logger LOGGER = LogManager.getLogger("App");

    public static void main(final String[] args) {
        try {
            LOGGER.info("Initializing Parking System App");
            ParkingInterface.loadInterface();
        } catch (ParkingServiceException e) {
            LOGGER.error("Unable to process App because of parking service exception ", e);
        }

    }
}
