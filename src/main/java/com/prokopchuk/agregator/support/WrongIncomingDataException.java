package com.prokopchuk.agregator.support;

public class WrongIncomingDataException extends Exception {

    public WrongIncomingDataException(String message) {
        super(message);
    }

    public WrongIncomingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}