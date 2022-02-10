package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class App {

    private static final Logger LOGGER = LogManager.getLogger("App");

    public static void main(final String[] args) {
        try {
            LOGGER.info("Initializing Parking System");
            InteractiveShell.loadInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
