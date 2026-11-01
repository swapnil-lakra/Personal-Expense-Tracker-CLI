package com.expensetracker.model;
import com.expensetracker.exception.InvalidCategoryException;

public enum Category {
    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName){
        this.displayName = displayName;
    }

    public static Category fromChoice(int choice) throws InvalidCategoryException {
        Category[] all = values();
        if(choice < 0 || choice > all.length){
            throw new InvalidCategoryException("Invalid category choice. Please select a number between 1 and "+all.length);
        }
        return Category.values()[choice-1];
    }

    public String displayName(){
        return this.displayName;
     }

    public static Category[] getAllChoices(){
        return values();
    }

}
