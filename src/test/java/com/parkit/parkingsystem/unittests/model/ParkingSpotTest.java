package com.parkit.parkingsystem.unittests.model;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ParkingSpotTest {

    @Test
    public void should_getAll_whenNewParkingSpot() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        assertEquals(1, parkingSpot.getId());
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
        assertFalse(parkingSpot.getIsAvailable());
    }

}
