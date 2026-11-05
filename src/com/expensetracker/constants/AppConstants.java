package com.expensetracker.constants;

/**
 * Centralized repository for application-wide constants.
 *
 * Defines commonly used configuration values, validation limits,
 * formatting rules, and display settings to maintain consistency
 * across different application components.
 *
 * This utility class is non-instantiable because it only provides
 * static constant values.
 */
public final class AppConstants {
    /**
     * Prevents instantiation of this utility class.
     */
    private AppConstants(){}

    /** Display name of the application. */
    public static final String APP_NAME = "Personal Expense Tracker";

    /** Current application version. */
    public static final String VERSION = "v1.0";

    /** Standard date format used throughout the application. */
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    /** Maximum permitted expense amount. */
    public static final double MAX_AMOUNT = 10_00_000.0;

    /** Maximum number of characters allowed in an expense description. */
    public static final int MAX_DESCRIPTION_LENGTH = 100;

    /** Number of highest-value expenses displayed in top expense reports. */
    public static final int TOP_EXPENSES_COUNT = 5;

    /** Standard width of menu separators in console output. */
    public static final int MENU_SEPARATOR_LENGTH = 60;
}
