package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.constants.CustomMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputReaderUtil {

    private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");

    private final Scanner scanner;

    public InputReaderUtil(final Scanner scanner) {
        this.scanner = scanner;
    }

    public int readSelection() {
        int result = -1;
        try {
            result = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error reading input. Please enter valid number for proceeding further");
        } catch (NoSuchElementException | IllegalStateException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_SCAN_ERROR, e);
            System.out.println(CustomMessages.MESSAGE_USER_SYSTEM_ERROR);
        }
        return result;

    }

    public String readVehicleRegistrationNumber() {
        String result = null;
        try {
            String vehicleRegNumber = scanner.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            } else {
                result = vehicleRegNumber;
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            LOGGER.error(CustomMessages.MESSAGE_LOG_SCAN_ERROR, e);
            System.out.println(CustomMessages.MESSAGE_USER_SYSTEM_ERROR);
        }
        return result;
    }
}
