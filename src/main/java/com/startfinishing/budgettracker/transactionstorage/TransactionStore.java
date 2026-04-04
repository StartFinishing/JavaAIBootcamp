package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.transaction.Transaction;
import java.util.List;

/**
 * Defines the contract for a transaction storage provider.
 *
 * <p>Implementations handle persistence and retrieval of transaction records, e.g., in-memory, CSV,
 * or database-backed solutions.
 */
public interface TransactionStore {

  /**
   * Adds a single transaction to the store.
   *
   * @param transaction The transaction to add. Must not be null.
   */
  public void addTransaction(Transaction transaction);

  /**
   * Adds multiple transactions to the store.
   *
   * @param transactions A list of transactions to add. Must not be null; implementations may choose
   *     how to handle empty lists.
   */
  public void addTransaction(List<? extends Transaction> transactions);

  /**
   * Retrieves all transactions currently stored.
   *
   * @return A copy of transactions. The returned list may be empty but will never be null.
   */
  public List<Transaction> getTransactions();
}
