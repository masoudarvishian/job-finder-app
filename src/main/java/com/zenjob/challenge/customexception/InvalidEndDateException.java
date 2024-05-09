package com.zenjob.challenge.customexception;

public class InvalidEndDateException extends RuntimeException {
    public InvalidEndDateException(String message) {
        super(message);
    }

    public InvalidEndDateException() {
    }
}
