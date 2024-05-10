package com.zenjob.challenge.customexception;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException() {
    }

    public InvalidActionException(String message) {
        super(message);
    }
}
