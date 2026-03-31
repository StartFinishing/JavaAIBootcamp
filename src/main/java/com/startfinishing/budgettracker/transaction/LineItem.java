package com.startfinishing.budgettracker.transaction;

import java.time.LocalDate;

/**
 * Immutable line item. To change a logical entry, construct a new {@code LineItem}.
 */
public class LineItem implements Transaction {

    private final String description;
    private final double amount;
    private final TransactionCategory category;
    private final LocalDate date;

    /**
     * Constructs a LineItem with a description, amount, category, and date.
     *
     * @param description A brief description of the line item (e.g., "Bus fare" or "Groceries").
     * @param amount      The monetary amount of the entry; must be strictly positive.
     * @param category    The classification for the transaction (see TransactionCategory enum).
     * @param date        The date the entry is recorded or applies to.
     * @throws IllegalArgumentException if amount is zero or negative
     */
    public LineItem(String description, double amount, TransactionCategory category, LocalDate date) {
        validateDescription(description);
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
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

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank");
        }

        for (char ch : description.toCharArray()) {
            if (!(Character.isLetterOrDigit(ch) || ch == ' ')) {
                throw new IllegalArgumentException(
                        "Description must contain only letters, digits, and spaces");
            }
        }
    }
}
