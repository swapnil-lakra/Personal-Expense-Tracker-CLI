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
import java.util.List;


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