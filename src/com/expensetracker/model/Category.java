package com.expensetracker.model;
import com.expensetracker.exception.InvalidCategoryException;

/**
 * Represents the predefined categories used to classify expenses.
 *
 * Each category maintains a user-friendly display name
 * for consistent presentation throughout the application.
 */
public enum Category {
    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    OTHER("Other");

    /** Human-readable name associated with the expense category. */
    private final String displayName;

     /**
     * Initializes an expense category with its display name.
     *
     * @param displayName user-friendly name of the category
     */
    Category(String displayName){
        this.displayName = displayName;
    }

    /**
     * Converts a one-based menu selection into its corresponding category.
     *
     * @param choice category selection entered by the user
     * @return the category corresponding to the selected choice
     * @throws InvalidCategoryException if the selection is outside the valid range
     */
    public static Category fromChoice(int choice) throws InvalidCategoryException {
        Category[] all = values();
        if(choice < 0 || choice > all.length){
            throw new InvalidCategoryException("Invalid category choice. Please select a number between 1 and "+all.length);
        }
        return Category.values()[choice-1];
    }

    /**
     * Returns the user-friendly display name of this category.
     *
     * @return display name of the category
     */
    public String displayName(){
        return this.displayName;
     }

     /**
     * Returns all available expense categories in declaration order.
     *
     * @return array containing all predefined categories
     */
    public static Category[] getAllChoices(){
        return values();
    }

}
