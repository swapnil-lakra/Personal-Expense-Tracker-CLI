package com.expensetracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.expensetracker.constants.AppConstants;
import com.expensetracker.exception.InvalidDateException;

public final class DateUtil {
    private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
    private DateUtil(){}

    public static LocalDate parse(String input) throws InvalidDateException {
        if(input == null || input.trim().isEmpty()){
            throw new InvalidDateException("Date cannot be empty");
        }

        try {
            return LocalDate.parse(input.trim(),FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("Invalid date. Please use format "+AppConstants.DATE_FORMAT);
        }
    }

    public static String format(LocalDate date){
        return date.format(FORMATTER);
    }

    public static boolean isFuture(LocalDate date){
        return date.isAfter(LocalDate.now());
    }
}
