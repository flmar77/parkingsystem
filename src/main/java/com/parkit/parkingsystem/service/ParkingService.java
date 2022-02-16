package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.exceptions.FareCalculatorException;
import com.parkit.parkingsystem.exceptions.ParkingServiceException;
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

    public void processIncomingVehicle() throws ParkingServiceException {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot == null) {
                throw new ParkingServiceException("Parking slots might be full");
            }

            String vehicleRegNumber = getVehicleRegNumber();

            parkingSpot.setIsAvailable(false);
            if (!parkingSpotDAO.updateParking(parkingSpot)) {
                throw new ParkingServiceException("error while updating parking spot to unavailable");
            }

            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber(vehicleRegNumber);
            ticket.setPrice(0);
            ticket.setInTime(new Date());
            ticket.setOutTime(null);
            if (ticketDAO.searchVehicleRegNumber(vehicleRegNumber)) {
                ticket.setDiscount(true);
                System.out.println("Welcome Back! As a recurring user of our parking lot, you'll benefit from a 5% discount");
            } else {
                ticket.setDiscount(false);
            }

            if (!ticketDAO.saveTicket(ticket)) {
                throw new ParkingServiceException("Unable to save ticket");
            }

            System.out.println("Generated Ticket and saved in DB");
            System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
            System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + ticket.getInTime());

        } catch (ParkingServiceException e) {
            LOGGER.error("Unable to process incoming vehicle", e);
            System.out.println("Unable to process incoming vehicle - sorry for the inconvenience and see you later !");
            throw e;
        }
    }

    public void processExitingVehicle() throws ParkingServiceException {
        try {
            String vehicleRegNumber = getVehicleRegNumber();

            Ticket ticket = ticketDAO.getCurrentTicket(vehicleRegNumber);
            if (ticket == null) {
                throw new ParkingServiceException("Unable to get current ticket");
            }

            ticket.setOutTime(new Date());
            fareCalculatorService.calculateFare(ticket);

            if (!ticketDAO.updateTicket(ticket)) {
                throw new ParkingServiceException("Unable to update ticket");
            }

            ParkingSpot parkingSpot = ticket.getParkingSpot();
            parkingSpot.setIsAvailable(true);
            if (!parkingSpotDAO.updateParking(parkingSpot)) {
                throw new ParkingServiceException("error while updating parking spot to available");
            }

            System.out.println("Please pay the parking fare:" + ticket.getPrice());
            System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + ticket.getOutTime());

        } catch (FareCalculatorException e) {
            throw new ParkingServiceException("Unable to process exiting vehicle because of fare calculator exception", e);
        } catch (ParkingServiceException e) {
            LOGGER.error("Unable to process exiting vehicle", e);
            System.out.println("Unable to process exiting vehicle - please push the red help button !");
            throw e;
        }
    }

    private ParkingSpot getNextParkingNumberIfAvailable() {
        ParkingSpot parkingSpot = null;
        ParkingType parkingType = getVehicleType();
        int parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        if (parkingNumber > 0) {
            parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
        } else {
            System.out.println("Parking slots might be full - see you later !");
        }
        return parkingSpot;
    }

    private ParkingType getVehicleType() {
        ParkingType parkingType = null;
        System.out.println("Please select vehicle type : 1 (for CAR) or 2 (for BIKE)");
        while (parkingType == null) {
            int input = inputReaderUtil.readSelection();
            switch (input) {
                case -1:
                    break;
                case 1:
                    parkingType = ParkingType.CAR;
                    break;
                case 2:
                    parkingType = ParkingType.BIKE;
                    break;
                default:
                    System.out.println("Incorrect input provided - Please select vehicle type : 1 (for CAR) or 2 (for BIKE)");
            }
        }
        return parkingType;
    }

    private String getVehicleRegNumber() {
        String result = null;
        System.out.println("Please type the vehicle registration number and press enter key");
        while (result == null) {
            result = inputReaderUtil.readVehicleRegistrationNumber();
        }
        return result;
    }


}
