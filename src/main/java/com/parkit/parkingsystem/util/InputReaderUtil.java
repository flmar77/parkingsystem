package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputReaderUtil {

    private static final Scanner scan = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");

    public int readSelection() throws NumberFormatException {
        int result;
        try {
            result = Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            throw e;
        }
        return result;

    }

    public String readVehicleRegistrationNumber() throws NoSuchElementException, IllegalStateException, IllegalArgumentException {
        try {
            String vehicleRegNumber = scan.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
