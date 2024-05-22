package com.job_finder.example.domain.exceptions;

public class InvalidEndDateException extends RuntimeException {
    public InvalidEndDateException(String message) {
        super(message);
    }

    public InvalidEndDateException() {
    }
}
