package com.zenjob.challenge.domain.exceptions;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException() {
    }

    public InvalidActionException(String message) {
        super(message);
    }
}
