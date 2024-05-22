package com.job_finder.example.domain.exceptions;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException() {
    }

    public InvalidActionException(String message) {
        super(message);
    }
}
