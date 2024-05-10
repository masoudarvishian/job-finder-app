package com.zenjob.challenge.domain.exceptions;

public class InvalidStartDateException extends RuntimeException {
    public InvalidStartDateException() {
    }

    public InvalidStartDateException(String message) {
        super(message);
    }
}
