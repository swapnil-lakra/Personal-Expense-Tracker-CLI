package com.expensetracker.service;

import com.expensetracker.exception.ExpenseNotFoundException;
import com.expensetracker.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Manages the application's expense records and provides business logic
 * for expense operations, filtering, reporting, and sorting.
 *
 * Uses an ArrayList for maintaining expense records and a HashMap
 * for efficient ID-based expense lookup.
 *
 * All data is maintained in memory during the application session.
 */
public class ExpenseManager {
    /** Stores expense records in insertion order. */
    private List<Expense> expenses;
    /** Provides efficient lookup of expenses using their unique IDs. */
    private Map<Integer, Expense> expenseById;
    /** Generates sequential unique identifiers for new expense records. */
    private int nextId;

    /**
     * Initializes the expense manager with empty collections
     * and sets the starting expense ID.
     */
    public ExpenseManager(){
        expenses = new ArrayList<>();
        expenseById = new HashMap<>();
        nextId = 1001;
    }

    /**
     * Creates and stores a new expense record.
     *
     * @param amount monetary value of the expense
     * @param category category assigned to the expense
     * @param date date on which the expense occurred
     * @param description additional expense details
     * @return the newly created expense record
     */
    public Expense addExpense(double amount,Category category, LocalDate date, String description){
        Expense expense = new Expense(nextId++, amount, category, date, description);
        expenses.add(expense);
        expenseById.put(expense.getId(), expense);
        return expense;
    }

    /**
     * Returns a copy of all stored expense records.
     *
     * @return a new list containing all expenses
     */
    public List<Expense> getAllExpenses(){
        return new ArrayList<>(expenses);
    }

    /**
     * Retrieves an expense using its unique identifier.
     *
     * @param id unique identifier of the requested expense
     * @return matching expense record
     * @throws ExpenseNotFoundException if no expense exists with the given ID
     */
    public Expense getExpenseById(int id) throws ExpenseNotFoundException {
        Expense expense = expenseById.get(id);
        if(expense == null){
            throw new ExpenseNotFoundException("Expense with ID "+id+" not found.");
        }
        return expense;
    }

    /**
     * Updates a specific field of an existing expense.
     *
     * Supported fields: AMOUNT, CATEGORY, DATE, DESCRIPTION.
     *
     * @param id identifier of the expense to update
     * @param field name of the field to update
     * @param newValue new value assigned to the selected field
     * @throws ExpenseNotFoundException if the expense does not exist
     * @throws IllegalArgumentException if the specified field is unsupported
     */
    public void updateExpense(int id, String field, Object newValue) throws ExpenseNotFoundException {
        Expense expense = getExpenseById(id);

        switch (field.toUpperCase()) {
            case "AMOUNT":
                expense.setAmount((Double) newValue);
                break;
            case "CATEGORY":
                expense.setCategory((Category) newValue);
                break;
            case "DATE":
                expense.setDate((LocalDate) newValue);
                break;
            case "DESCRIPTION":
                expense.setDescription((String) newValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown update field: "+field);
        }
    }

    /**
     * Removes an expense from both the list and ID lookup map.
     *
     * @param id identifier of the expense to delete
     * @return true if the expense was deleted; false if it was not found
     */
    public boolean deleteExpense(int id){
        Expense removed = expenseById.remove(id);
        if(removed == null) return false;
        expenses.remove(removed.getId());
        return true;
    }

    /**
     * Filters expenses by their assigned category.
     *
     * @param category category used as the filtering criterion
     * @return list of expenses matching the specified category
     */
    public List<Expense> filterByCategory(Category category){
        List<Expense> result = new ArrayList<>();
        for(Expense e : expenses){
            if(e.getCategory() == category){
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Retrieves expenses occurring within an inclusive date range.
     *
     * @param from starting date of the range
     * @param to ending date of the range
     * @return list of expenses within the specified date range
     */
    public List<Expense> filterByDateRange(LocalDate from, LocalDate to){
        List<Expense> result = new ArrayList<>();
        for(Expense e: expenses){
            LocalDate d = e.getDate();
            if(!d.isBefore(from) && !d.isAfter(to)){
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Searches expense descriptions using a case-insensitive keyword match.
     *
     * @param keyword text to search for in expense descriptions
     * @return list of expenses whose descriptions contain the keyword
     */
    public List<Expense> filterByKeyword(String keyword){
        List<Expense> result = new ArrayList<>();
        if(keyword == null || keyword.trim().isEmpty()){
            return result;
        }

        String lower = keyword.toLowerCase();
        for(Expense e: expenses){
            String desc = e.getDescription();
            if(desc != null && desc.toLowerCase().contains(lower)){
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Filters expenses whose amounts fall within an inclusive range.
     *
     * @param min minimum acceptable expense amount
     * @param max maximum acceptable expense amount
     * @return list of expenses within the specified amount range
     */
    public List<Expense> filterByAmountRange(double min,double max){
        List<Expense> result = new ArrayList<>();
        for(Expense e: expenses){
            double amount = e.getAmount();
            if(amount >= min && amount <= max){
                result.add(e);
            }
        }
        return result;
    }

     /**
     * Calculates the cumulative amount of all recorded expenses.
     *
     * @return total spending across all expense records
     */
    public double getTotal(){
        double total = 0.0;
        for(Expense e: expenses){
            total += e.getAmount();
        }
        return total;
    }

    /**
     * Calculates aggregate spending for each expense category.
     *
     * @return map containing categories and their corresponding totals
     */
    public Map<Category,Double> getCategoryWiseTotal(){
        Map<Category,Double> total = new HashMap<>();
        for(Expense e: expenses){
            total.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return total;
    }

     /**
     * Generates a monthly spending report in chronological order.
     *
     * @return sorted map containing year-month keys and monthly totals
     */
    public Map<String,Double> getMonthlyReport(){
        Map<String,Double> report = new TreeMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for(Expense e: expenses){
            String key = e.getDate().format(fmt);
            report.merge(key, e.getAmount(), Double::sum);
        }
        return report;
    }

    /**
     * Retrieves the highest-value expenses in descending amount order.
     *
     * @param n maximum number of expenses to retrieve
     * @return list containing up to n highest-value expenses
     */
    public List<Expense> getTopNExpenses(int n){
        if(n <= 0){
            return  new ArrayList<>();
        }

        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        if(n >= sorted.size()){
            return sorted;
        }

        return new ArrayList<>(sorted.subList(0, n));
    }

    /**
     * Calculates the average amount spent per expense record.
     *
     * @return average expense amount, or zero if no expenses exist
     */
    public double getAverageExpense(){
        if(expenses.isEmpty()){
            return 0.0;
        }
        return getTotal() / expenses.size();
    }

    /**
     * Sorts expense records by the specified field and direction.
     *
     * Supported fields: DATE, AMOUNT, CATEGORY.
     *
     * @param field field used as the sorting criterion
     * @param ascending true for ascending order; false for descending order
     * @return a new list containing sorted expense records
     * @throws IllegalArgumentException if the sorting field is unsupported
     */
    public List<Expense> sortBy(String field,boolean ascending){
        List<Expense> sorted = new ArrayList<>(expenses);
        Comparator<Expense> comparator;

        switch (field.toUpperCase()) {
            case "DATE":
                comparator = Comparator.comparing(Expense::getDate);
                break;
            case "AMOUNT":
                comparator = Comparator.comparing(Expense::getAmount);
                break;
            case "CATEGORY":
                comparator = Comparator.comparing(Expense::getCategory);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort field: "+field);
        }

        if(!ascending){
            comparator = comparator.reversed();
        }

        sorted.sort(comparator);
        return sorted;
    }

}
