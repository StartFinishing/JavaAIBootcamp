package com.startfinishing.budgettracker.transaction;

import java.time.LocalDate;

/** Immutable line item. To change a logical entry, construct a new {@code LineItem}. */
public class LineItem implements Transaction {

  private final String description;
  private final double amount;
  private final TransactionCategory category;
  private final LocalDate date;

  /**
   * Constructs a LineItem with a description, amount, category, and date.
   *
   * @param description A brief description of the line item (e.g., "Bus fare" or "Groceries").
   * @param amount The monetary amount of the entry; must be strictly positive.
   * @param category The classification for the transaction (see TransactionCategory enum).
   * @param date The date the entry is recorded or applies to.
   *     <p>Does not validate the description, amount, category, or date. This is done in the
   *     validateTransaction method of the MemoryStorage class if required.
   */
  public LineItem(String description, double amount, TransactionCategory category, LocalDate date) {
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
