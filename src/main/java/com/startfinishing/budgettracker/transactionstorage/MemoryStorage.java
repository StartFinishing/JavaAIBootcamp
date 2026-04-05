package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MemoryStorage implements TransactionStore {

  /**
   * European day/month/year: variable-width day and month, then 4-digit or 2-digit year ({@code uu}
   * uses pivot around 2000).
   */
  private static final List<DateTimeFormatter> FLEXIBLE_DATE_FORMATTERS =
      List.of(
          DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ROOT),
          DateTimeFormatter.ofPattern("d/M/uu", Locale.ROOT));

  private final List<Transaction> transactions = new ArrayList<>();
  private static final MemoryStorage instance = new MemoryStorage();

  public static MemoryStorage getInstance() {
    return instance;
  }

  MemoryStorage() {}

  @Override
  public void addTransaction(Transaction transaction) {
    validateTransaction(transaction);
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  @Override
  public void addTransaction(List<? extends Transaction> incoming) {
    if (incoming == null) {
      throw new IllegalArgumentException("Transactions cannot be null");
    }
    for (Transaction transaction : incoming) {
      validateTransaction(transaction);
    }
    this.transactions.addAll(incoming);
  }

  @Override
  public Transaction addTransaction(
      String description, String amountString, String category, String dateString)
      throws IllegalArgumentException, NumberFormatException, DateTimeParseException {
    double amount = Double.parseDouble(amountString);
    LocalDate date = parseFlexibleDate(dateString);
    TransactionCategory categoryEnum =
        TransactionCategory.valueOf(category.toUpperCase(Locale.ROOT));
    if (categoryEnum == null) {
      throw new IllegalArgumentException("Invalid category: " + category);
    }
    Transaction transaction = new LineItem(description, amount, categoryEnum, date);
    validateTransaction(transaction);
    addTransaction(transaction);
    return transaction;
  }

  private static LocalDate parseFlexibleDate(String dateString) {
    String text = dateString.trim();
    DateTimeParseException last = null;
    for (DateTimeFormatter formatter : FLEXIBLE_DATE_FORMATTERS) {
      try {
        return LocalDate.parse(text, formatter);
      } catch (DateTimeParseException e) {
        last = e;
      }
    }
    if (last != null) {
      throw last;
    }
    throw new DateTimeParseException("Empty date text", text, 0);
  }

  private static void validateTransaction(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }
    if (transaction.getAmount() <= 0) {
      throw new IllegalArgumentException("Transaction amount must be greater than 0");
    }
    if (transaction.getDate() == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }
    if (transaction.getDescription() == null || transaction.getDescription().isEmpty()) {
      throw new IllegalArgumentException("Transaction description cannot be null or empty");
    }
  }
}
