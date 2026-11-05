package com.expensetracker.exception;

/**
 * Custom checked exception thrown when an expense amount
 * does not satisfy the application's validation rules.
 *
 * This exception allows the application to handle invalid
 * monetary inputs explicitly and provide meaningful feedback.
 */
public class InvalidAmountException extends Exception {
    /** Unique version identifier for serialization compatibility. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs an exception with a descriptive error message.
     *
     * @param message explanation of why the expense amount is invalid
     */
    public InvalidAmountException(String message){
        super(message);
    }

    /**
     * Constructs an exception with an error message and underlying cause.
     *
     * @param message explanation of the validation failure
     * @param cause underlying exception that caused this exception
     */
    public InvalidAmountException(String message,Throwable cause){
        super(message,cause);
    }
}
