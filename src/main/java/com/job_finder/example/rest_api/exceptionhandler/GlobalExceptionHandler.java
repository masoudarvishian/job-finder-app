package com.job_finder.example.rest_api.exceptionhandler;

import com.job_finder.example.domain.exceptions.InvalidActionException;
import com.job_finder.example.domain.exceptions.InvalidEndDateException;
import com.job_finder.example.domain.exceptions.InvalidStartDateException;
import com.job_finder.example.rest_api.dto.ErrorResponseDto;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidStartDateException.class)
    ResponseEntity<Object> handleCustomException(InvalidStartDateException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEndDateException.class)
    ResponseEntity<Object> handleCustomException(InvalidEndDateException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidActionException.class)
    ResponseEntity<Object> handleCustomException(InvalidActionException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleCustomException(NotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleCustomException(IllegalArgumentException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

