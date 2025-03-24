package com.powerbi.api.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions and maps them to appropriate HTTP responses.
 * 
 * Handles exceptions such as:
 * - DataIntegrityViolationException: Returns a 409 Conflict status.
 * - AccessDeniedException: Returns a 403 Forbidden status.
 * - NoSuchElementException: Returns a 404 Not Found status.
 * - ResourceNotFoundException: Returns a 404 Not Found status.
 * - RuntimeException: Returns a 500 Internal Server Error status.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles DataIntegrityViolationException and returns a 409 Conflict response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles AccessDeniedException and returns a 403 Forbidden response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles NoSuchElementException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>("No such element found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles RuntimeException and returns a 500 Internal Server Error response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with the error message and HTTP status
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
