package com.example.documentmanagement.exception;

/**
 * Custom exception class to handle cases where a requested document is not found in the system.
 * This exception extends the RuntimeException class and provides constructors to pass error messages and causes.
 */
public class DocumentNotFoundException extends RuntimeException {

    /**
     * Constructor for DocumentNotFoundException with a specific error message.
     *
     * @param message The error message to be associated with this exception.
     */
    public DocumentNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for DocumentNotFoundException with a specific error message and a cause.
     *
     * @param message The error message to be associated with this exception.
     * @param cause   The cause of the exception, which can be used to retrieve the stack trace.
     */
    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
