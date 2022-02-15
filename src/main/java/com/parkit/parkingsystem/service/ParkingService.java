package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.exceptions.FareCalculatorException;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    private static final Logger LOGGER = LogManager.getLogger("ParkingService");

    private final FareCalculatorService fareCalculatorService;
    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;

    public ParkingService(final InputReaderUtil inputReaderUtil, final ParkingSpotDAO parkingSpotDAO, final TicketDAO ticketDAO, final FareCalculatorService fareCalculatorService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.fareCalculatorService = fareCalculatorService;
    }

    public void processIncomingVehicle() throws Exception {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            String vehicleRegNumber = getVehicleRegNumber();
            parkingSpot.setIsAvailable(false);
            parkingSpotDAO.updateParking(parkingSpot); //allot this parking space and mark its availability as false

            Date inTime = new Date();
            Ticket ticket = new Ticket();
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, DISCOUNT)
            //ticket.setId(ticketID);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber(vehicleRegNumber);
            ticket.setPrice(0);
            ticket.setInTime(inTime);
            ticket.setOutTime(null);
            if (ticketDAO.searchVehicleRegNumber(vehicleRegNumber)) {
                ticket.setDiscount(true);
                System.out.println("Welcome Back! As a recurring user of our parking lot, you'll benefit from a 5% discount");
            } else {
                ticket.setDiscount(false);
            }

            if (ticketDAO.saveTicket(ticket)) {
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
            } else {
                throw new Exception("Unable to save ticket");
            }

        } catch (Exception ex) {
            LOGGER.error("Unable to process incoming vehicle", ex);
            throw ex;
        }
    }

    private String getVehicleRegNumber() {
        String result = null;
        System.out.println("Please type the vehicle registration number and press enter key");
        while (result == null) {
            result = inputReaderUtil.readVehicleRegistrationNumber();
        }
        return result;
    }

    private ParkingSpot getNextParkingNumberIfAvailable() throws Exception {
        int parkingNumber;
        ParkingSpot parkingSpot;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
            throw ie;
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
            throw e;
        }
        return parkingSpot;
    }

    private ParkingType getVehicleType() throws IllegalArgumentException {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1:
                return ParkingType.CAR;
            case 2:
                return ParkingType.BIKE;
            default:
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    public void processExitingVehicle() throws Exception {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getCurrentTicket(vehicleRegNumber);
            if (ticket == null) {
                throw new Exception("Unable to get current ticket");
            }
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setIsAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
                throw new Exception("Unable to update ticket information. Error occurred");
            }
        } catch (FareCalculatorException e) {
            LOGGER.error("Unable to calculate fare", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
            throw e;
        }
    }
}
