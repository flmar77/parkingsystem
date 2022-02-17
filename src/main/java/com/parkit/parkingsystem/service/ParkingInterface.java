package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.exceptions.ParkingServiceException;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public abstract class ParkingInterface {

    private static final Logger LOGGER = LogManager.getLogger("ParkingInterface");

    //TODO : utiliser getClass().getResourceAsStream()
    private static final String DATABASE_CONFIG_FILEPATH = "src/main/java/com/parkit/parkingsystem/config/dataBaseConfigProd.properties";

    public static void loadInterface() throws ParkingServiceException {
        LOGGER.info("Interface initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;
        Scanner scanner = new Scanner(System.in, "UTF-8");
        InputReaderUtil inputReaderUtil = new InputReaderUtil(scanner);
        DataBaseConfigService dataBaseConfigService = new DataBaseConfigService(DATABASE_CONFIG_FILEPATH);
        DataBaseService dataBaseService = new DataBaseService(dataBaseConfigService.getDataBaseConfig());
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(dataBaseService);
        TicketDAO ticketDAO = new TicketDAO(dataBaseService);
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);


        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case -1:
                    break;
                case 1:
                    parkingService.processIncomingVehicle();
                    break;
                case 2:
                    parkingService.processExitingVehicle();
                    break;
                case 3:
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                default:
                    System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }

    private static void loadMenu() {
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }
}
