package com.parkit.parkingsystem.util;

import java.util.Scanner;

public class InputReaderUtil {

    private final Scanner scanner;

    public InputReaderUtil(final Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * @return int from System.in if successfully, else -1
     */
    public int readSelection() {
        int result = -1;
        try {
            result = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // do nothing : let the default result (-1) and the caller send a reminder to the user
        }
        return result;
    }

    /**
     * @return String from System.in if successfully, else null
     */
    public String readVehicleRegistrationNumber() {
        String result = null;
        String vehicleRegNumber = scanner.nextLine();
        if (vehicleRegNumber != null && vehicleRegNumber.trim().length() > 0) {
            result = vehicleRegNumber;
        }
        return result;
    }
}
