
package com.expensetracker.util;

import java.time.LocalDate;

import com.expensetracker.constants.AppConstants;

/**
 * Utility class providing reusable console formatting and display methods.
 *
 * Centralizes common presentation operations such as printing headers,
 * separators, formatted monetary values, dates, and percentages.
 *
 * This class promotes consistent console output and avoids
 * duplication of formatting logic across application components.
 *
 * This is a non-instantiable utility class containing static methods.
 */
public final class ConsoleUtil {

    /**
     * Prevents instantiation of this utility class.
     */
    private ConsoleUtil() {}

    /**
     * Generates a string containing the specified number of spaces.
     *
     * @param count number of spaces to generate
     * @return string containing the requested spaces, or an empty string
     *         if the count is non-positive
     */
    private static String spaces(int count) {
        if (count <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(count);

        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }

        return sb.toString();
    }

    /**
     * Prints a horizontal separator using the specified character.
     *
     * @param length number of characters in the separator
     * @param symbol character used to construct the separator
     */
    public static void printLine(int length, char symbol) {
        if (length <= 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(symbol);
        }

        System.out.println(sb.toString());
    }

    /**
     * Prints a horizontal separator using the default '=' character.
     *
     * @param length number of characters in the separator
     */
    public static void printLine(int length) {
        printLine(length, '=');
    }

    /**
     * Prints a horizontal separator using the application's
     * configured default separator length.
     */
    public static void printLine() {
        printLine(AppConstants.MENU_SEPARATOR_LENGTH, '=');
    }

    /**
     * Displays a centered section header surrounded by horizontal separators.
     *
     * @param title title to display in the console header
     */
    public static void printHeader(String title) {
        printLine();

        if (title != null && !title.isEmpty()) {
            int width = AppConstants.MENU_SEPARATOR_LENGTH;
            int padding = Math.max(0, (width - title.length()) / 2);

            System.out.println(spaces(padding) + title);
        } else {
            System.out.println();
        }

        printLine();
    }

    /**
     * Formats a monetary amount with two decimal places.
     *
     * @param amount monetary value to format
     * @return formatted amount with the Rs. currency prefix
     */
    public static String formatAmount(double amount) {
        return String.format("Rs. %.2f", amount);
    }

    /**
     * Formats a LocalDate using the application's standard date format.
     *
     * @param date date to format
     * @return formatted date string
     */
    public static String formatDate(LocalDate date) {
        return DateUtil.format(date);
    }

    /**
     * Formats a percentage value with one decimal place.
     *
     * @param percent percentage value to format
     * @return formatted percentage string with a percentage symbol
     */
    public static String formatPercentage(double percent) {
        return String.format("%.1f%%", percent);
    }
}
