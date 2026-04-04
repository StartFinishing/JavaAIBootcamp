package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;

class MemoryStorage implements TransactionStore {

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
