package com.expensetracker.exception;

/**
 * Custom checked exception thrown when an expense record
 * cannot be found using the specified identifier.
 *
 * This exception helps the application handle missing expense
 * records explicitly during retrieval, update, and deletion operations.
 */
public class ExpenseNotFoundException extends Exception {

    /** Unique version identifier for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception with a descriptive error message.
     *
     * @param message explanation of why the expense was not found
     */
    public ExpenseNotFoundException(String message){
        super(message);
    }

    /**
     * Constructs an exception with an error message and underlying cause.
     *
     * @param message explanation of the failure
     * @param cause underlying exception that caused this exception
     */
    public ExpenseNotFoundException(String message,Throwable cause){
        super(message,cause);
    }
}
