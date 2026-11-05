
package com.expensetracker.util;

import java.time.LocalDate;
import java.util.Scanner;

import com.expensetracker.constants.AppConstants;
import com.expensetracker.exception.InvalidAmountException;
import com.expensetracker.exception.InvalidCategoryException;
import com.expensetracker.exception.InvalidDateException;
import com.expensetracker.model.Category;

/**
 * Handles user input collection and validation for the expense tracking
 * application.
 *
 * Provides reusable methods for validating monetary amounts, expense
 * categories, dates, menu selections, expense identifiers, descriptions,
 * and confirmation responses.
 *
 * Uses a shared Scanner instance to maintain consistent console input
 * processing throughout the application.
 */
public class InputValidator {

    /** Scanner instance used to read user input from the console. */
    private final Scanner scanner;

    /**
     * Initializes the validator with an existing Scanner instance.
     *
     * @param sc Scanner used for console input
     * @throws IllegalArgumentException if the supplied Scanner is null
     */
    public InputValidator(Scanner sc) {
        if (sc == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        scanner = sc;
    }

    /**
     * Reads and validates a monetary expense amount.
     *
     * Ensures that the input is numeric, finite, positive,
     * and within the application's configured maximum limit.
     *
     * @param prompt message displayed before reading the amount
     * @return validated expense amount
     * @throws InvalidAmountException if the input is empty, non-numeric,
     *         non-finite, non-positive, or exceeds the permitted limit
     */
    public double readAmount(String prompt) throws InvalidAmountException {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            throw new InvalidAmountException("Amount cannot be empty");
        }

        double amount;

        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a valid number: ", e);
        }

        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new InvalidAmountException("Amount must be a finite number.");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }

        if (amount > AppConstants.MAX_AMOUNT) {
            throw new InvalidAmountException(
                "Amount cannot exceed Rs. " + AppConstants.MAX_AMOUNT
            );
        }

        return amount;
    }

    /**
     * Reads and validates an expense category selection.
     *
     * Converts the numeric menu selection into its corresponding
     * Category enum constant.
     *
     * @return selected expense category
     * @throws InvalidCategoryException if the input is empty,
     *         non-numeric, or outside the available category range
     */
    public Category readCategory() throws InvalidCategoryException {
        int total = Category.getAllChoices().length;
        System.out.println("Enter category choice (1-" + total + "): ");

        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            throw new InvalidCategoryException("Category choice cannot be empty");
        }

        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidCategoryException("Category choice must be a number.", e);
        }

        return Category.fromChoice(choice);
    }

    /**
     * Reads and validates an expense date.
     *
     * An empty input defaults to the current system date.
     * Future dates are not permitted.
     *
     * @param prompt message displayed before reading the date
     * @return validated expense date
     * @throws InvalidDateException if the date cannot be parsed
     *         or represents a future date
     */
    public LocalDate readDate(String prompt) throws InvalidDateException {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return LocalDate.now();
        }

        LocalDate date = DateUtil.parse(input);

        if (DateUtil.isFuture(date)) {
            throw new InvalidDateException("Future date is not allowed");
        }

        return date;
    }

    /**
     * Repeatedly prompts the user until a valid menu selection is entered.
     *
     * @param min minimum permitted menu choice
     * @param max maximum permitted menu choice
     * @return validated menu selection
     * @throws IllegalArgumentException if the minimum exceeds the maximum
     */
    public int readMenuChoice(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot exceed max");
        }

        while (true) {
            System.out.println("Enter your choices (" + min + "-" + max + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("[ERROR] Input cannot be empty.");
                continue;
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid number.");
                continue;
            }

            if (choice < min || choice > max) {
                System.out.println(
                    "[ERROR] Choice must be between " + min + " and " + max + "."
                );
                continue;
            }

            return choice;
        }
    }

    /**
     * Reads an expense identifier and ensures that it is a positive integer.
     *
     * Re-prompts the user when the input is empty, non-numeric,
     * or non-positive.
     *
     * @param prompt message displayed before reading the expense ID
     * @return validated positive expense identifier
     */
    public int readExpenseId(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("[ERROR] Expense ID must be a number");
                continue;
            }

            int id;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Expense ID must be a number.");
                continue;
            }

            if (id <= 0) {
                System.out.println("[ERROR] Expense ID must be positive number.");
                continue;
            }

            return id;
        }
    }

    /**
     * Reads and validates an optional expense description.
     *
     * Ensures that the description does not exceed the configured
     * maximum character limit.
     *
     * @param prompt message displayed before reading the description
     * @return trimmed description entered by the user
     */
    public String readDescription(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();

            if (input.length() > AppConstants.MAX_DESCRIPTION_LENGTH) {
                System.out.println(
                    "[ERROR] Description cannot exceed "
                    + AppConstants.MAX_DESCRIPTION_LENGTH
                    + " characters. Current: " + input.length()
                );
                continue;
            }

            return input;
        }
    }

    /**
     * Reads a confirmation response from the user.
     *
     * Accepts 'y' or 'yes' as confirmation and 'n' or 'no'
     * as rejection, ignoring letter case.
     *
     * @param prompt message displayed before reading confirmation
     * @return true if the user confirms; false if the user rejects
     */
    public boolean readConfirmation(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("[ERROR] Please enter 'y' or 'n'.");
        }
    }
}
