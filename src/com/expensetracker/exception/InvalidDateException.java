package com.expensetracker.exception;

/**
 * Custom checked exception thrown when an invalid date is provided
 * during expense creation, update, or date-based search operations.
 *
 * This exception enables explicit handling of date validation failures
 * and helps the application provide meaningful feedback to users.
 */
public class InvalidDateException extends Exception {

    /** Unique version identifier for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception with a descriptive error message.
     *
     * @param message explanation of why the provided date is invalid
     */
    public InvalidDateException(String message){
        super(message);
    }

    /**
     * Constructs an exception with an error message and underlying cause.
     *
     * @param message explanation of the date validation failure
     * @param cause underlying exception that caused this exception
     */
    public InvalidDateException(String message, Throwable cause){
        super(message, cause);
    }
}
