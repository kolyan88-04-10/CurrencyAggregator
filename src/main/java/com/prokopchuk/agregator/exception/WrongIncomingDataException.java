package com.prokopchuk.agregator.exception;

public class WrongIncomingDataException extends Exception {

    public WrongIncomingDataException(String message) {
        super(message);
    }

    public WrongIncomingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}