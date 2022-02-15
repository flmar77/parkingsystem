package com.parkit.parkingsystem.exceptions;

public class FareCalculatorException extends Exception {

    public FareCalculatorException(String message) {
        super(message);
    }

    public FareCalculatorException(String message, Exception e) {
        super(message, e);
    }
}
