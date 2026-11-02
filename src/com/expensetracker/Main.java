package com.expensetracker;

import java.time.LocalDate;
import java.util.*;

import com.expensetracker.service.ExpenseManager;
import com.expensetracker.ui.Menu;
import com.expensetracker.util.*;
import com.expensetracker.constants.AppConstants;
import com.expensetracker.model.*;
import com.expensetracker.exception.*;

public class Main {

    private final Scanner scanner;
    private final ExpenseManager manager;
    private final InputValidator validator;
    private final Menu menu;
    private boolean running;

    public Main(){
        this.scanner = new Scanner(System.in);
        this.manager = new ExpenseManager();
        this.validator = new InputValidator(scanner);
        this.menu = new Menu();
        this.running = true;
    }

    // Entry Point
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    private void run(){
        while(running){
            menu.showMainMenu();
            int choice = validator.readMenuChoice(1, 8);

            switch (choice) {
                case 1: handleAddExpense(); break;
                case 2: handleViewAll(); break;
                case 3: handleSearch(); break;
                //case 4: handleUpdate(); break;
                //case 5: handleDelete(); break;
                //case 6: handleReport(); break;
                //case 7: handleSort(); break;
                case 8: running = false; break;
                default:
                    menu.showError("Invalid Choice.");
            }
        }
        shutdown();
    }

    // Cleanup and farewell.
    private void shutdown(){
        ConsoleUtil.printHeader("THANK YOU");
        menu.showSuccess("Goodbye! Have a great day.");
        scanner.close();
    }

    // === Main Menu Handlers ===

    private void handleAddExpense(){
        ConsoleUtil.printHeader("ADD NEW EXPENSE");
        try {
            double amount = validator.readAmount("Enter Amount (Rs .): ");
            menu.showCategoryMenu();
            Category category = validator.readCategory();
            String description = validator.readDescription("Enter description (optional, max "+
                AppConstants.MAX_DESCRIPTION_LENGTH + " chars): "
            );
            LocalDate date = validator.readDate("Enter date (dd-MM-yyyy) [Enter = today]: ");
            Expense expense = manager.addExpense(amount, category, date, description);
            menu.showSuccess("Expense added successfully! (ID: "+expense.getId()+")");
        } catch (InvalidAmountException | InvalidCategoryException | InvalidDateException ex) {
            menu.showError(ex.getMessage());
        }
        pause();
    }

    private void handleViewAll(){
        List<Expense> list = manager.getAllExpenses();
        menu.showExpenseTable(list);
        pause();
    }

    private void handleSearch(){
        while(true){
            menu.showSearchMenu();
            int choice = validator.readMenuChoice(1, 5);
            switch(choice){
                case 1: searchByCategory(); break;
                case 2: searchByDateRange(); break;
                case 3: searchByKeyword(); break;
                case 4: searchByAmountRange(); break;
                case 5: return;
            }
        }
    }

    // === Search sub-handlers ===

    private void searchByCategory(){
        try {
            menu.showCategoryMenu();
            Category category = validator.readCategory();
            List<Expense> result = manager.filterByCategory(category);
            displayExpenses(result, "EXPENSES IN CATEGORY: "+category.displayName().toUpperCase());
        } catch (InvalidCategoryException ex) {
            menu.showError(ex.getMessage());
            pause();
        }
    }

    private void searchByDateRange(){
        try {
            LocalDate from = validator.readDate("Enter FROM date (dd-MM-yyyy): ");
            LocalDate to = validator.readDate("Enter TO date (dd-MM-yyyy): ");
            if(from.isAfter(to)){
                menu.showError("From date cannot be after To date");
                pause();
                return;
            }
            List<Expense> result = manager.filterByDateRange(from, to);
            displayExpenses(result, "EXPENSES FROM " + ConsoleUtil.formatDate(from) + " TO " + ConsoleUtil.formatDate(to));
        } catch (InvalidDateException ex) {
            menu.showError(ex.getMessage());
            pause();
        }
    }

    private void searchByKeyword(){
        System.out.print("Enter Keyword to search in description: ");
        String keyword = scanner.nextLine().trim();
        if(keyword.isEmpty()){
            menu.showError("Keyword cannnot be empty.");
            pause();
            return;
        }
        List<Expense> result = manager.filterByKeyword(keyword);
        displayExpenses(result, "EXPENSES MATCHING '" + keyword + "'");
    }

    private void searchByAmountRange(){
        try {
            double min = validator.readAmount("Enter MIN amount (Rs.): ");
            double max = validator.readAmount("Enter MAX amount (Rs.): ");

            if(min > max){
                menu.showError("Min amount cannot be greater than Max amount.");
                pause();
                return;
            }
            List<Expense> result = manager.filterByAmountRange(min, max);
            displayExpenses(result, "EXPENSE BETWEEN " +
             ConsoleUtil.formatAmount(min) +
              " AND " +
               ConsoleUtil.formatAmount(max));  
        } catch (InvalidAmountException ex) {
            menu.showError(ex.getMessage());
            pause();
        }
    }



    // === Display helpers ===
    private void displayExpenses(List<Expense> list, String title){
        menu.showExpenseTable(list,title);
        pause();
    }

    private void pause(){
        menu.pressEnterToContinue(scanner);
    }


}
