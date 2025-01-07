package com.example.documentmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("details", request.getDescription(false));
            logger.error("Exception occurred: {}", ex.getMessage(), ex);
        } catch (Exception loggingException) {
            logger.error("Error while logging exception: {}", loggingException.getMessage(), loggingException);
        } finally {
            // Any cleanup code can be placed here if needed
        }
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("details", request.getDescription(false));
            logger.error("Resource not found: {}", ex.getMessage(), ex);
        } catch (Exception loggingException) {
            logger.error("Error while logging resource not found exception: {}", loggingException.getMessage(), loggingException);
        } finally {
            // Any cleanup code can be placed here if needed
        }
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public final ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("details", request.getDescription(false));
            logger.error("Invalid input: {}", ex.getMessage(), ex);
        } catch (Exception loggingException) {
            logger.error("Error while logging invalid input exception: {}", loggingException.getMessage(), loggingException);
        } finally {
            // Any cleanup code can be placed here if needed
        }
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
