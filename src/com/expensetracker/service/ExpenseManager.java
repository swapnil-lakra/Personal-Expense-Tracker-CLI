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

public class ExpenseManager {
    private List<Expense> expenses;
    private Map<Integer, Expense> expenseById;
    private int nextId;

    public ExpenseManager(){
        expenses = new ArrayList<>();
        expenseById = new HashMap<>();
        nextId = 1001;
    }

    public Expense addExpense(double amount,Category category, LocalDate date, String description){
        Expense expense = new Expense(nextId++, amount, category, date, description);
        expenses.add(expense);
        expenseById.put(expense.getId(), expense);
        return expense;
    }

    public List<Expense> getAllExpenses(){
        return new ArrayList<>(expenses);
    }

    public Expense getExpenseById(int id) throws ExpenseNotFoundException {
        Expense expense = expenseById.get(id);
        if(expense == null){
            throw new ExpenseNotFoundException("Expense with ID "+id+" not found.");
        }
        return expense;
    }

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

    public boolean deleteExpense(int id){
        Expense removed = expenseById.remove(id);
        if(removed == null) return false;
        expenses.remove(removed);
        return true;
    }

    public List<Expense> filterByCategory(Category category){
        List<Expense> result = new ArrayList<>();
        for(Expense e : expenses){
            if(e.getCategory() == category){
                result.add(e);
            }
        }
        return result;
    }

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

    public double getTotal(){
        double total = 0.0;
        for(Expense e: expenses){
            total += e.getAmount();
        }
        return total;
    }

    public Map<Category,Double> getCategoryWiseTotal(){
        Map<Category,Double> total = new HashMap<>();
        for(Expense e: expenses){
            total.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return total;
    }

    public Map<String,Double> getMonthlyReport(){
        Map<String,Double> report = new TreeMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for(Expense e: expenses){
            String key = e.getDate().format(fmt);
            report.merge(key, e.getAmount(), Double::sum);
        }
        return report;
    }

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

    public double getAverageExpense(){
        if(expenses.isEmpty()){
            return 0.0;
        }
        return getTotal() / expenses.size();
    }

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
