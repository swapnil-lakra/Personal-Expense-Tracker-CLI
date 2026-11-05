
package com.expensetracker.ui;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.expensetracker.constants.AppConstants;
import com.expensetracker.util.ConsoleUtil;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

/**
 * Handles the presentation layer of the Personal Expense Tracker.
 *
 * Responsible for displaying navigation menus, expense records,
 * financial reports, sorting options, and user feedback messages.
 *
 * Delegates formatting operations to ConsoleUtil and uses model
 * objects to present expense-related information without handling
 * business logic or data management.
 */
public class Menu {

    /**
     * Creates a Menu instance for displaying application interfaces.
     */
    public Menu() {}

    /** Formatter used to convert year-month values into readable month labels. */
    private static final DateTimeFormatter MONTH_FORMATTER =
            DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    /**
     * Displays the main navigation menu with all available application features.
     */
    public void showMainMenu() {
        ConsoleUtil.printHeader(AppConstants.APP_NAME + " " + AppConstants.VERSION);
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Search / Filter Expense");
        System.out.println("4. Update Expense");
        System.out.println("5. Delete Expense");
        System.out.println("6. Reports & Analytics");
        System.out.println("7. Sort Expenses");
        System.out.println("8. Exit");
        ConsoleUtil.printLine();
    }

    /**
     * Displays all predefined expense categories with their corresponding
     * one-based menu selection numbers.
     */
    public void showCategoryMenu() {
        System.out.println("Select Category:");

        Category[] categories = Category.getAllChoices();

        for (int i = 0; i < categories.length; i++) {
            System.out.printf("   %d. %s%n", i + 1, categories[i].displayName());
        }
    }

    /**
     * Displays available expense search and filtering options.
     */
    public void showSearchMenu() {
        ConsoleUtil.printHeader("SEARCH / FILTER EXPENSES");
        System.out.println("1. By Category");
        System.out.println("2. By Date Range");
        System.out.println("3. By Keyword");
        System.out.println("4. By Amount Range");
        System.out.println("5. Back to Main Menu");
        ConsoleUtil.printLine();
    }

    /**
     * Displays available financial reporting and analytics options.
     */
    public void showReportMenu() {
        ConsoleUtil.printHeader("REPORTS AND ANALYTICS");
        System.out.println("1. Total Spending");
        System.out.println("2. Category-wise Breakdown");
        System.out.println("3. Monthly Report");
        System.out.println("4. Date Range Report");
        System.out.println("5. Top Highest Expenses");
        System.out.println("6. Average Expense");
        System.out.println("7. Back to Main Menu");
        ConsoleUtil.printLine();
    }

    /**
     * Displays sorting options for arranging expenses by date,
     * amount, or category in ascending or descending order.
     */
    public void showSortMenu() {
        ConsoleUtil.printHeader("SORT EXPENSES");
        System.out.println("1. Date (ascending)");
        System.out.println("2. Date (descending)");
        System.out.println("3. Amount (ascending)");
        System.out.println("4. Amount (descending)");
        System.out.println("5. Category (ascending)");
        System.out.println("6. Category (descending)");
        System.out.println("7. Back to Main Menu");
        ConsoleUtil.printLine();
    }

    /**
     * Displays the available fields that can be modified
     * when updating an existing expense.
     */
    public void showUpdateFieldMenu() {
        ConsoleUtil.printHeader("UPDATE EXPENSES");
        System.out.println("1. Update Amount");
        System.out.println("2. Update Category");
        System.out.println("3. Update Date");
        System.out.println("4. Update Description");
        System.out.println("5. Cancel");
        ConsoleUtil.printLine();
    }

    /**
     * Displays expense records in a formatted console table.
     *
     * Calculates and displays the combined amount and number of
     * expenses included in the supplied list.
     *
     * @param expenses list of expense records to display
     * @param title heading displayed above the expense table
     */
    public void showExpenseTable(List<Expense> expenses, String title) {
        ConsoleUtil.printHeader(title);

        if (expenses == null || expenses.isEmpty()) {
            showInfo("No expenses to display");
            return;
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        System.out.printf("%-8s %-12s %-16s %-12s %s%n",
                "ID", "Date", "Category", "Amount", "Description");

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        double total = 0.0;

        for (Expense e : expenses) {
            String desc = (e.getDescription() == null ||
                    e.getDescription().isEmpty()) ? "-" : e.getDescription();

            System.out.printf("%-8d %-12s %-16s %12s %s%n",
                    e.getId(),
                    ConsoleUtil.formatDate(e.getDate()),
                    e.getCategory().displayName(),
                    ConsoleUtil.formatAmount(e.getAmount()),
                    desc
            );

            total += e.getAmount();
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        System.out.println("Total: " + ConsoleUtil.formatAmount(total)
                + " (" + expenses.size() + " expense(s))");
    }

    /**
     * Displays expense records using the default table heading.
     *
     * @param expenses list of expense records to display
     */
    public void showExpenseTable(List<Expense> expenses) {
        showExpenseTable(expenses, "ALL EXPENSES");
    }

    /**
     * Displays the highest-value expense records with a dynamically
     * generated heading based on the number of records provided.
     *
     * @param topExpenses list of highest-value expenses
     */
    public void showTopExpenses(List<Expense> topExpenses) {
        int count = (topExpenses == null || topExpenses.isEmpty())
                ? 0 : topExpenses.size();

        showExpenseTable(topExpenses, "TOP " + count + " HIGHEST EXPENSES");
    }

    /**
     * Displays a category-wise spending breakdown, including each
     * category's total amount and percentage contribution.
     *
     * @param categoryTotal map containing categories and their spending totals
     * @param total combined spending across all expense categories
     */
    public void showCategoryReport(Map<Category, Double> categoryTotal, double total) {
        ConsoleUtil.printHeader("CATEGORY-WISE BREAKDOWN");

        if (categoryTotal == null || categoryTotal.isEmpty()) {
            showInfo("No expenses recorded yet.");
            return;
        }

        List<Map.Entry<Category, Double>> entries =
                new ArrayList<>(categoryTotal.entrySet());

        entries.sort(Map.Entry.<Category, Double>comparingByKey().reversed());

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        for (Map.Entry<Category, Double> entry : entries) {
            double pct = (total > 0)
                    ? (entry.getValue() / total) * 100.0 : 0.0;

            System.out.printf("%-16s %14s %8s%n",
                    entry.getKey().displayName(),
                    ConsoleUtil.formatAmount(entry.getValue()),
                    ConsoleUtil.formatPercentage(pct));
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        System.out.printf("%-16s %14s %8s%n",
                "TOTAL",
                ConsoleUtil.formatAmount(total),
                ConsoleUtil.formatPercentage(100.0));
    }

    /**
     * Displays monthly spending totals using readable month labels.
     *
     * @param monthlyTotal map containing year-month keys and spending totals
     * @param total combined spending across all months
     */
    public void showMonthlyReport(Map<String, Double> monthlyTotal, double total) {
        ConsoleUtil.printHeader("MONTHLY REPORT");

        if (monthlyTotal == null || monthlyTotal.isEmpty()) {
            showInfo("No expenses recorded yet.");
            return;
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        for (Map.Entry<String, Double> entry : monthlyTotal.entrySet()) {
            System.out.printf("%-16s %14s%n",
                    prettyMonth(entry.getKey()),
                    ConsoleUtil.formatAmount(entry.getValue()));
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH, '-');

        System.out.printf("%-16s %14s%n",
                "TOTAL",
                ConsoleUtil.formatAmount(total));
    }

    /**
     * Displays a success message to indicate that an operation
     * has completed successfully.
     *
     * @param message success message to display
     */
    public void showSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    /**
     * Displays an error message when an operation cannot be completed.
     *
     * @param message error details to display
     */
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Displays general informational feedback to the user.
     *
     * @param message informational message to display
     */
    public void showInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Pauses application navigation until the user presses Enter.
     *
     * @param scanner Scanner instance used to read the user's input
     */
    public void pressEnterToContinue(Scanner scanner) {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Converts a year-month key into a human-readable month label.
     *
     * If the supplied key cannot be parsed, the original value
     * is returned as a fallback.
     *
     * @param key month identifier in yyyy-MM format
     * @return formatted month label or the original key if parsing fails
     */
    private String prettyMonth(String key) {
        try {
            YearMonth ym = YearMonth.parse(key);
            return ym.format(MONTH_FORMATTER);
        } catch (DateTimeParseException e) {
            return key;
        }
    }
}
