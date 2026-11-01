package com.expensetracker.util;

import java.time.LocalDate;
import java.util.Scanner;

import com.expensetracker.constants.AppConstants;
import com.expensetracker.exception.InvalidAmountException;
import com.expensetracker.exception.InvalidCategoryException;
import com.expensetracker.exception.InvalidDateException;
import com.expensetracker.model.Category;

public class InputValidator {
    private final Scanner scanner;

    public InputValidator(Scanner sc){
        if(sc == null){
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        scanner = sc;
    }

    public double readAmount(String prompt) throws InvalidAmountException {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        if(input.isEmpty()){
            throw new InvalidAmountException("Amount cannot be empty");
        }

        double amount;

        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a valid number: ",e);
        }

        if(Double.isNaN(amount) || Double.isInfinite(amount)){
            throw new InvalidAmountException("Amount must be a finite number.");
        }

        if(amount <= 0){
            throw new InvalidAmountException("Amount must be greater than zero.");
        }

        if(amount > AppConstants.MAX_AMOUNT){
            throw new InvalidAmountException("Amount cannot exceed Rs. "+AppConstants.MAX_AMOUNT);
        }

        return amount;

    }

    public Category readCategory() throws InvalidCategoryException {
        int total = Category.getAllChoices().length;
        System.out.println("Enter category choice (1-"+total+"): ");
        String input = scanner.nextLine().trim();

        if(input.isEmpty()){
            throw new InvalidCategoryException("Category choice cannot be empty");
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidCategoryException("Category choice must be a number.",e);
        }
        
        return Category.fromChoice(choice);

    }

    public LocalDate readDate(String prompt) throws InvalidDateException {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();

        if(input.isEmpty()){
            return LocalDate.now();
        }

        LocalDate date = DateUtil.parse(input);
        if(DateUtil.isFuture(date)){
            throw new InvalidDateException("Future date is not allowed");
        }

        return date;
    }

    public int readMenuChoice(int min,int max){
        if(min > max){
            throw new IllegalArgumentException("min cannot exceed max");
        }

        while(true){
            System.out.println("Enter your choices ("+min+"-"+max+"): ");
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
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

            if(choice < min || choice > max){
                System.out.println("[ERROR] Choice must be between "+min+" and "+max+".");
                continue;
            }

            return choice;
        }
    }

    public int readExpenseId(String prompt){
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            
            if(input.isEmpty()){
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

            if(id <= 0){
                System.out.println("[ERROR] Expense ID must be positive number.");
                continue;
            }
            return id;
        }
    }

    public String readDescription(String prompt){
        while(true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();

            if(input.length() > AppConstants.MAX_DESCRIPTION_LENGTH){
                System.out.println("[ERROR] Description cannot exceed "+AppConstants.MAX_DESCRIPTION_LENGTH+" characters. Current: "+input.length());
                continue;
            }

            return input;
        }
    }

    public boolean readConfirmation(String prompt){
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if(input.equals("y") || input.equals("yes")){
                return true;
            }else if(input.equals("n") || input.equals("no")){
                return false;
            }

            System.out.println("[ERROR] Please enter 'y' or 'n'.");
        }
    }

    
}
