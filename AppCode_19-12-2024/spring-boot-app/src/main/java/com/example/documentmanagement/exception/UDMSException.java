package com.example.documentmanagement.exception;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom exception class for the Unified Data Management System (UDMS).
 * This class extends the RuntimeException and provides constructors
 * to handle different exception scenarios.
 */
public class UDMSException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(UDMSException.class);
    private final String methodName;
    private final LocalDateTime loggedAt;

    /**
     * Constructor for UDMSException with a message.
     *
     * @param message the detail message.
     * @param methodName the name of the method where the exception occurred.
     */
    public UDMSException(String message, String methodName) {
        super(message);
        this.methodName = methodName;
        this.loggedAt = LocalDateTime.now();
        logError(message);
    }

    /**
     * Constructor for UDMSException with a message and a cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception.
     * @param methodName the name of the method where the exception occurred.
     */
    public UDMSException(String message, Throwable cause, String methodName) {
        super(message, cause);
        this.methodName = methodName;
        this.loggedAt = LocalDateTime.now();
        logError(message);
    }

    /**
     * Logs the error message along with the method name and timestamp.
     *
     * @param message the error message to be logged.
     */
    private void logError(String message) {
        logger.error("Error in method {}: {}. Logged at: {}", methodName, message, loggedAt);
        // Here you can also add code to save the error details to a database or external system if needed.
    }

    /**
     * Gets the method name where the exception occurred.
     *
     * @return the method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Gets the timestamp when the exception was logged.
     *
     * @return the timestamp.
     */
    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }
}
