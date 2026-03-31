package com.startfinishing.budgettracker;

import java.time.LocalDate;

/**
 * Read-only view of a budget line: description, amount, category, and date.
 */
public interface BudgetTransaction {

    String getDescription();

    double getAmount();

    TransactionCategory getCategory();

    LocalDate getDate();
}
