package com.example.documentmanagement.exception;

/**
 * Custom exception class for handling email-related errors in the document management application.
 * This exception is thrown when email operations fail, providing a specific error message.
 */
public class EmailException extends Exception {

    /**
     * Constructs a new EmailException with the specified detail message.
     *
     * @param message the detail message.
     */
    public EmailException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmailException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
