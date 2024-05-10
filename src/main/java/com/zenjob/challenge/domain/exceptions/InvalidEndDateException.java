package com.zenjob.challenge.domain.exceptions;

public class InvalidEndDateException extends RuntimeException {
    public InvalidEndDateException(String message) {
        super(message);
    }

    public InvalidEndDateException() {
    }
}
