package com.startfinishing.budgettracker;

import java.time.LocalDate;

/**
 * Immutable budget entry. To change a logical entry, construct a new {@code BudgetEntry}.
 */
public class BudgetEntry implements BudgetTransaction {

    private final String description;
    private final double amount;
    private final TransactionCategory category;
    private final LocalDate date;

    /**
     * Constructs a BudgetEntry with a description, amount, category, and date.
     *
     * @param description A brief description of the budget entry (e.g., "Bus fare" or "Groceries").
     * @param amount      The monetary amount of the entry; must be strictly positive.
     * @param category    The classification for the transaction (see TransactionCategory enum).
     * @param date        The date the entry is recorded or applies to.
     * @throws BudgetEntryException if amount is zero or negative
     */
    public BudgetEntry(String description, double amount, TransactionCategory category, LocalDate date)
            throws BudgetEntryException {
        if (amount <= 0) {
            throw new BudgetEntryException("Amount must be positive");
        }
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public TransactionCategory getCategory() {
        return category;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }
}
