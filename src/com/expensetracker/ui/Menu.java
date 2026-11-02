package com.expensetracker.ui;
// 
// +-------------------------------------------------------------------+
// |                          <<class>> Menu                           |
// +-------------------------------------------------------------------+
// | (no instance fields)                                              |
// +-------------------------------------------------------------------+
// | + Menu()                                                          |
// |                                                                   |
// | // Navigation menus (display only)                                |
// | + showMainMenu(): void                                            |
// | + showCategoryMenu(): void                                        |
// | + showSearchMenu(): void                                          |
// | + showReportMenu(): void                                          |
// | + showSortMenu(): void                                            |
// | + showUpdateFieldMenu(): void                                     |
// |                                                                   |
// | // Expense displays                                               |
// | + showExpenseTable(List<Expense>, String title): void             |
// | + showExpenseTable(List<Expense>): void                           |
// | + showTopExpenses(List<Expense>): void                            |
// |                                                                   |
// | // Report displays                                                |
// | + showCategoryReport(Map<Category, Double>, double total): void   |
// | + showMonthlyReport(Map<String, Double>, double total): void      |
// |                                                                   |
// | // Message helpers                                                |
// | + showSuccess(String): void                                       |
// | + showError(String): void                                         |
// | + showInfo(String): void                                          |
// | + pressEnterToContinue(Scanner): void                             |
// |                                                                   |
// | // Private helpers                                                |
// | - prettyMonth(String key): String                                 |
// +-------------------------------------------------------------------+
// 

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.expensetracker.constants.AppConstants;
import com.expensetracker.util.ConsoleUtil;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

public class Menu {
    public Menu(){}

    private static final DateTimeFormatter MONTH_FORMATTER = 
            DateTimeFormatter.ofPattern("MMM yyyy",Locale.ENGLISH);
    
    // Navigation menu (display only)        
    public void showMainMenu(){
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

    public void showCategoryMenu(){
        System.out.println("Select Category:");
        Category[] categories = Category.getAllChoices();
        for(int i = 0;i < categories.length;i++){
            System.out.printf("   %d. %s%n", i + 1, categories[i].displayName());
        }
    }

    public void showSearchMenu(){
        ConsoleUtil.printHeader("SEARCH / FILTER EXPENSES");
        System.out.println("1. By Category");
        System.out.println("2. By Date Range");
        System.out.println("3. By Keyword");
        System.out.println("4. By Amount Range");
        System.out.println("5. Back to Main Menu");
        ConsoleUtil.printLine();
    }

    public void showReportMenu(){
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

    public void showSortMenu(){
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

    public void showUpdateFieldMenu(){
        ConsoleUtil.printHeader("UPDATE EXPENSES");
        System.out.println("1. Update Amount");
        System.out.println("2. Update Category");
        System.out.println("3. Update Date");
        System.out.println("4. Update Description");
        System.out.println("5. Cancel");
        ConsoleUtil.printLine();
    }

    // Expense Display
    public void showExpenseTable(List<Expense> expenses, String title){
        ConsoleUtil.printHeader(title);

        if(expenses == null || expenses.isEmpty()){
            showInfo("No expenses to display");
            return;
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        System.out.printf("%-8s %-12s %-16s %-12s %s%n","ID",
        "Date","Category","Amount","Description");
        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');

        double total = 0.0;
        for(Expense e : expenses){
            String desc = (e.getDescription() == null || 
            e.getDescription().isEmpty()) ? "-" : 
            e.getDescription();
            
            System.out.printf("%-8d %-12s %-16s %12s %s%n",
                e.getId(), ConsoleUtil.formatDate(e.getDate()),
                e.getCategory().displayName(), 
                ConsoleUtil.formatAmount(e.getAmount()),
                desc
            );

            total += e.getAmount();
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        System.out.println("Total: " + ConsoleUtil.formatAmount(total) 
        + " (" + expenses.size() + " expense(s))");
    }

    public void showExpenseTable(List<Expense> expenses){
        showExpenseTable(expenses,"ALL EXPENSES");
    }

    public void showTopExpenses(List<Expense> topExpenses){
        int count = (topExpenses == null || topExpenses.isEmpty()) ? 0 : topExpenses.size();
        showExpenseTable(topExpenses,"TOP "+count+" HIGHEST EXPENSES");
    }

    // Report Display
    public void showCategoryReport(Map<Category,Double> categoryTotal, double total){
        ConsoleUtil.printHeader("CATEGORY-WISE BREAKDOWN");

        if(categoryTotal == null || categoryTotal.isEmpty()){
            showInfo("No expenses recorded yet.");
            return;
        }

        List<Map.Entry<Category, Double>> entries = 
        new ArrayList<>(categoryTotal.entrySet());

        entries.sort(Map.Entry.<Category, Double>comparingByKey().reversed());

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        for(Map.Entry<Category, Double> entry : entries){
            double pct = (total > 0) ? (entry.getValue() / total) * 100.0 : 0.0;

            System.out.printf("%-16s %14s %8s%n",
                entry.getKey().displayName(),
                ConsoleUtil.formatAmount(entry.getValue()),
                ConsoleUtil.formatPercentage(pct));
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        System.out.printf("%-16s %14s %8s%n",
            "TOTAL",
            ConsoleUtil.formatAmount(total),
            ConsoleUtil.formatPercentage(100.0)
        );
    }

    public void showMonthlyReport(Map<String, Double> monthlyTotal, double total){
        ConsoleUtil.printHeader("MONTHLY REPORT");

        if(monthlyTotal == null || monthlyTotal.isEmpty()){
            showInfo("No expenses recorded yet.");
            return;
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        for(Map.Entry<String,Double> entry : monthlyTotal.entrySet()){
            System.out.printf("%-16s %14%n",
                prettyMonth(entry.getKey()),
                ConsoleUtil.formatAmount(entry.getValue())
            );
        }

        ConsoleUtil.printLine(AppConstants.MENU_SEPARATOR_LENGTH,'-');
        System.out.printf("%-16s %14s%n",
            "TOTAL",
            ConsoleUtil.formatAmount(total)
        );
    }

    // Message helpers
    public void showSuccess(String message){
        System.out.println("[OK] " + message);
    }

    public void showError(String message){
        System.out.println("[ERROR] " + message);
    }

    public void showInfo(String message){
        System.out.println("[INFO] " + message);
    }

    public void pressEnterToContinue(Scanner scanner){
        System.out.println("Press Enter to continue...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            
        }
    }

    // Private Helpers
    private String prettyMonth(String key){
        try {
            YearMonth ym = YearMonth.parse(key);
            return ym.format(MONTH_FORMATTER);
        } catch (Exception e) {
            return key;
        }
    }

    
}