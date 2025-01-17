package com.parkit.parkingsystem.unittests.util;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static Scanner scanner;

    @Test
    public void should_returnPositiveInt_whenReadSelectionAndSetInInt() throws NumberFormatException {
        when(scanner.nextLine()).thenReturn("1");
        inputReaderUtil = new InputReaderUtil(scanner);

        assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test
    public void should_returnNegativeInt_whenReadSelectionAndSetInString() throws NumberFormatException {
        when(scanner.nextLine()).thenReturn("ABC");
        inputReaderUtil = new InputReaderUtil(scanner);

        assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test
    public void should_returnString_whenReadVehicleRegistrationNumberAndSetInString() throws NoSuchElementException, IllegalStateException, IllegalArgumentException {
        when(scanner.nextLine()).thenReturn("ABC");
        inputReaderUtil = new InputReaderUtil(scanner);

        assertEquals("ABC", inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    public void should_returnNull_whenReadVehicleRegistrationNumberAndSetInNothing() throws NoSuchElementException, IllegalStateException, IllegalArgumentException {
        when(scanner.nextLine()).thenReturn("");
        inputReaderUtil = new InputReaderUtil(scanner);

        assertNull(inputReaderUtil.readVehicleRegistrationNumber());
    }

}
