package com.expensetracker.model;

import java.time.LocalDate;

/**
 * Represents an individual expense record in the expense tracking system.
 *
 * Each expense contains a unique identifier, monetary amount, category,
 * transaction date, and optional description.
 *
 * Encapsulation is maintained through private fields and public
 * getter and setter methods.
 */
public class Expense {
    
    /** Unique identifier assigned to each expense record. */
    private final int id;

    /** Monetary amount associated with the expense. */
    private double amount;

    /** Category used to classify the expense. */
    private Category category;

    /** Date on which the expense was incurred. */
    private LocalDate date;

    /** Optional description providing additional expense details. */
    private String description;

     /**
     * Creates a new expense record with the specified details.
     *
     * @param id unique identifier of the expense
     * @param amount monetary value of the expense
     * @param category category associated with the expense
     * @param date date when the expense occurred
     * @param description additional details about the expense
     */
    public Expense(int id, double amount, Category category, LocalDate date, String description) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    /**
     * Returns the unique identifier of this expense.
     *
     * @return expense identifier
     */
    public int getId(){
        return id;
    }

    /**
     * Returns the monetary amount of this expense.
     *
     * @return expense amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the category assigned to this expense.
     *
     * @return expense category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the date on which this expense occurred.
     *
     * @return expense date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the additional description of this expense.
     *
     * @return expense description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the monetary amount of this expense.
     *
     * @param amount new expense amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Updates the category assigned to this expense.
     *
     * @param category new expense category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Updates the date of this expense.
     *
     * @param date new expense date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Updates the description of this expense.
     *
     * @param description new expense description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
