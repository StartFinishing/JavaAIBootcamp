package com.startfinishing.budgettracker;

import com.startfinishing.budgettracker.BudgetEntryException;
import com.startfinishing.budgettracker.TransactionCategory;
import java.time.LocalDate;



public class BudgetEntry {
    private String description;
    private double amount;
    private TransactionCategory category;
    private LocalDate date;

    /**
     * Constructs a BudgetEntry with a description, amount, category, and date.
     *
     * @param description A brief description of the budget entry (e.g., "Bus fare" or "Groceries").
     * @param amount      The monetary amount of the entry. Positive for income, negative for expenses.
     * @param category    The classification for the transaction (see TransactionCategory enum).
     * @param date        The date the entry is recorded or applies to.
     */
    public BudgetEntry(String description, double amount, TransactionCategory category, LocalDate date) 
    throws BudgetEntryException {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        if (amount <= 0) 
            throw new BudgetEntryException("Amount must be positive");
    }

    /**
     * Returns the description of the budget entry.
     * @return the description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the budget entry.
     * @param description the new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the amount of the budget entry.
     * @return the monetary amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the budget entry.
     * Ensures the amount is non-negative. Throws BudgetEntryException if invalid.
     * 
     * @param amount the new monetary amount to set
     * @throws BudgetEntryException if amount is negative
     */
    public void setAmount(double amount) throws BudgetEntryException {
        if (amount < 0) {
            throw new BudgetEntryException("Amount must be non-negative");
        }
        this.amount = amount;
    }

    /**
     * Returns the category of the budget entry.
     * @return the transaction category
     */
    public TransactionCategory getCategory() {
        return category;
    }

    /**
     * Sets the category of the budget entry.
     * @param category the new transaction category to set
     */
    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    /**
     * Returns the date of the budget entry.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the budget entry.
     * @param date the new date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}   