package com.job_finder.example.domain.exceptions;

public class InvalidStartDateException extends RuntimeException {
    public InvalidStartDateException() {
    }

    public InvalidStartDateException(String message) {
        super(message);
    }
}
