package com.zenjob.challenge.customexception;

public class InvalidStartDateException extends RuntimeException {
    public InvalidStartDateException() {
    }

    public InvalidStartDateException(String message) {
        super(message);
    }
}
