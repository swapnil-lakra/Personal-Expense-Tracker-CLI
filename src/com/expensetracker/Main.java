package com.expensetracker;

import java.time.LocalDate;
import java.util.Scanner;

import com.expensetracker.service.ExpenseManager;
import com.expensetracker.ui.Menu;
import com.expensetracker.util.ConsoleUtil;
import com.expensetracker.util.InputValidator;
import com.expensetracker.constants.AppConstants;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
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
                //case 2: handleViewAll(); break;
                //case 3: handleSearch(); break;
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

    private void pause(){
        menu.pressEnterToContinue(scanner);
    }


}
