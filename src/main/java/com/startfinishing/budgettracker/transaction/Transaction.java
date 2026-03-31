package com.startfinishing.budgettracker.transaction;

import java.time.LocalDate;

/**
 * Transaction line item: description, amount, category, and date.
 */
public interface Transaction {

    String getDescription();
    double getAmount();
    TransactionCategory getCategory();
    LocalDate getDate();
}
