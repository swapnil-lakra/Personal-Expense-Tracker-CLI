package com.expensetracker.exception;

/**
 * Custom checked exception thrown when an invalid expense category
 * is selected or provided during expense operations.
 *
 * This exception enables explicit handling of category validation
 * failures and helps provide meaningful feedback to users.
 */
public class InvalidCategoryException extends Exception {
    
    /** Unique version identifier for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs an exception with a descriptive error message.
     *
     * @param message explanation of why the category selection is invalid
     */
    public InvalidCategoryException(String message){
        super(message);
    }

    /**
     * Constructs an exception with an error message and underlying cause.
     *
     * @param message explanation of the category validation failure
     * @param cause underlying exception that caused this exception
     */
    public InvalidCategoryException(String message, Throwable cause){
        super(message,cause);
    }

}