package com.example.documentmanagement.exception;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom exception class to handle signature-related operations.
 * This exception is thrown when there is a failure in signature operations.
 */
public class SignatureException extends Exception {
    private static final Logger LOGGER = Logger.getLogger(SignatureException.class.getName());
    private final String methodName;
    private final String errorMessage;
    private final LocalDateTime loggedAt;

    /**
     * Constructor for SignatureException.
     *
     * @param message The error message associated with the exception.
     * @param methodName The name of the method where the exception occurred.
     */
    public SignatureException(String message, String methodName) {
        super(message);
        this.methodName = methodName;
        this.errorMessage = message;
        this.loggedAt = LocalDateTime.now();
        logError();
    }

    /**
     * Logs the error details to the console and potentially to a persistent store.
     */
    private void logError() {
        try {
            // Log the error details
            LOGGER.log(Level.SEVERE, "Error in method: {0} at {1} - {2}", new Object[]{methodName, loggedAt, errorMessage});
            
            // Here you can add code to log the error to a database or external system if needed
            // For example, using a UDMS API to log errors
            // logErrorToUDMS();

        } catch (Exception e) {
            // Handle any exceptions that occur during logging
            LOGGER.log(Level.SEVERE, "Failed to log error: {0}", e.getMessage());
        } finally {
            // Any cleanup code if necessary
        }
    }

    // Example method to demonstrate how you might log to an external system
    // private void logErrorToUDMS() {
    //     // Implement the logic to log the error to the Unified Data Management System (UDMS)
    // }
}
