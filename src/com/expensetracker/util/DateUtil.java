package com.expensetracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.expensetracker.constants.AppConstants;
import com.expensetracker.exception.InvalidDateException;

/**
 * Utility class providing reusable date parsing, formatting,
 * and validation operations for the expense tracking application.
 *
 * Uses the application's centralized date format to maintain
 * consistent date handling across different components.
 *
 * This class cannot be instantiated because it provides
 * only static utility methods.
 */
public final class DateUtil {
    /** Formatter configured with the application's standard date format. */
    private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
    
    /**
     * Prevents instantiation of this utility class.
     */        
    private DateUtil(){}

    /**
     * Parses a date string into a LocalDate object.
     *
     * Leading and trailing whitespace is removed before parsing.
     *
     * @param input date string in the configured application format
     * @return parsed LocalDate instance
     * @throws InvalidDateException if input is null, empty, or cannot be parsed
     */
    public static LocalDate parse(String input) throws InvalidDateException {
        if(input == null || input.trim().isEmpty()){
            throw new InvalidDateException("Date cannot be empty");
        }

        try {
            return LocalDate.parse(input.trim(),FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date. Please use format "+AppConstants.DATE_FORMAT,e);
        }
    }

    /**
     * Formats a LocalDate object into the application's standard date format.
     *
     * @param date date to be formatted
     * @return formatted date string
     */
    public static String format(LocalDate date){
        return date.format(FORMATTER);
    }

    /**
     * Checks whether the specified date occurs after the current system date.
     *
     * @param date date to validate
     * @return true if the date is in the future; otherwise false
     */
    public static boolean isFuture(LocalDate date){
        return date.isAfter(LocalDate.now());
    }
}
