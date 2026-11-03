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
                case 4: handleUpdate(); break;
                case 5: handleDelete(); break;
                case 6: handleReport(); break;
                case 7: handleSort(); break;
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

    private void handleUpdate(){
        ConsoleUtil.printHeader("UPDATE HEADER");

        List<Expense> list = manager.getAllExpenses();
        if(list.isEmpty()){
            menu.showInfo("No Expenses to update.");
            pause();
            return;
        }
        
        menu.showExpenseTable(list,"AVAILABLE EXPENSES");

        int id = validator.readExpenseId("Enter expense ID to update: ");

        try {
            manager.getExpenseById(id);
        } catch (ExpenseNotFoundException ex) {
            menu.showError(ex.getMessage());
            pause();
            return;
        }

        menu.showUpdateFieldMenu();
        int choice = validator.readMenuChoice(1, 5);
        switch(choice){
            case 1: updateAmount(id); break;
            case 2: updateCategory(id); break;
            case 3: updateDate(id); break;
            case 4: updateDescription(id); break;
            case 5: menu.showInfo("Update cancelled."); break;
        }
        pause();
    }

    private void handleDelete(){
        ConsoleUtil.printHeader("DELETE EXPENSE");
        int id = validator.readExpenseId("Enter expense ID to delete: ");

        //preview first
        try {
            Expense expense = manager.getExpenseById(id);
            menu.showExpenseTable(Collections.singletonList(expense),"EXPENSE TO DELETE");
        } catch (ExpenseNotFoundException ex) {
            menu.showError(ex.getMessage());
            pause();
            return;
        }

        boolean confirm = validator.readConfirmation("Are you sure you want to delete? (y/n): ");
        if(!confirm){
            menu.showInfo("Delete cancelled");
            pause();
            return;
        }

        if(manager.deleteExpense(id)){
            menu.showSuccess("Expense deleted successfully.");
        }else{
            menu.showError("Delete failed - expense not found.");
        }
        pause();
    }

    private void handleReport(){
        while(true){
            menu.showReportMenu();
            int choice = validator.readMenuChoice(1, 7);
            switch(choice){
                case 1: reportTotal(); break;
                case 2: reportCategoryWise(); break;
                case 3: reportMonthy(); break;
                case 4: reportDateRange(); break;
                case 5: reportTopExpense(); break;
                case 6: reportAverage(); break;
                case 7: return; // back to main menu
            }
            pause();
        }
    }

    private void handleSort() {
        menu.showSortMenu();
        int choice = validator.readMenuChoice(1, 7);
        switch (choice) {
            case 1: performSort("DATE", true); break;
            case 2: performSort("DATE", false); break;
            case 3: performSort("AMOUNT", true); break;
            case 4: performSort("AMOUNT", false); break;
            case 5: performSort("CATEGORY", true); break;
            case 6: performSort("CATEGORY", false); break;
            case 7: return; // back
        }
        pause();
    }

    // === Sort helper ===
    private void performSort(String field, boolean ascending) {
        try {
            List<Expense> sorted = manager.sortBy(field, ascending);
            String direction = ascending ? "ASC" : "DESC";
            menu.showExpenseTable(sorted, "SORTED BY " + field + " (" + direction + ")");
        } catch (IllegalArgumentException ex) {
            menu.showError("Sort failed: " + ex.getMessage());
        }
    }

    // === report sub-handlers ===
    private void reportTotal(){
        double total = manager.getTotal();
        ConsoleUtil.printHeader("TOTAL SPENDING");
        menu.showInfo("Total spent: "+ConsoleUtil.formatAmount(total) + 
                       " across " + manager.getAllExpenses().size() + " expense(s).");
    }

    private void reportCategoryWise(){
        Map<Category,Double> categoryTotals = manager.getCategoryWiseTotal();
        double total = manager.getTotal();
        menu.showCategoryReport(categoryTotals, total);
    }

    private void reportMonthy(){
        Map<String,Double> monthlyTotals = manager.getMonthlyReport();
        double total = manager.getTotal();
        menu.showMonthlyReport(monthlyTotals, total);
    }

    private void reportDateRange(){
        try {
            LocalDate from = validator.readDate("Enter FROM date (dd-MM-yyyy)");
            LocalDate to = validator.readDate("Enter TO date (dd-MM-yyyy)");
            if(from.isAfter(to)){
                menu.showError("From date cannot be after To date.");
                return;
            }
            List<Expense> result = manager.filterByDateRange(from, to);
            menu.showExpenseTable(result, "EXPENSES FROM "+ConsoleUtil.formatDate(from)+" TO "+ConsoleUtil.formatDate(to));
        } catch (InvalidDateException ex) {
            menu.showError(ex.getMessage());
        }
    }

    private void reportTopExpense(){
        List<Expense> top = manager.getTopNExpenses(AppConstants.TOP_EXPENSES_COUNT);
        menu.showTopExpenses(top);
    }

    private void reportAverage(){
        double avg = manager.getAverageExpense();
        ConsoleUtil.printHeader("AVERAGE EXPENSE");
        menu.showInfo("Average per expense: "+ConsoleUtil.formatAmount(avg));
    }


    // === Update sub-handlers ===
    private void updateAmount(int id){
        try {
            double newAmount = validator.readAmount("Enter new amount (Rs.): ");
            manager.updateExpense(id, "AMOUNT", newAmount);
            menu.showSuccess("Amount updated successfully.");
        } catch (InvalidAmountException | ExpenseNotFoundException ex) {
            menu.showError(ex.getMessage());
        }
    }

    private void updateCategory(int id){
        try {
            menu.showCategoryMenu();
            Category newCategory = validator.readCategory();
            manager.updateExpense(id, "CATEGORY", newCategory);
            menu.showSuccess("Category updated successfully.");
        } catch (InvalidCategoryException | ExpenseNotFoundException ex) {
            menu.showError(ex.getMessage());
        }
    }

    private void updateDate(int id){
        try{
            LocalDate newDate = validator.readDate("Enter new date (dd-MM-yyyy): ");
            manager.updateExpense(id, "DATE", newDate);
            menu.showSuccess("Date updated successfully.");
        }catch(InvalidDateException | ExpenseNotFoundException ex){
            menu.showError(ex.getMessage());
        }
    }

    private void updateDescription(int id){
        try {
            String newDesc = validator.readDescription("Enter new description: ");
            manager.updateExpense(id, "DESCRIPTION", newDesc);
            menu.showSuccess("Description updated successfully.");
        } catch (ExpenseNotFoundException ex) {
            menu.showError(ex.getMessage());
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
