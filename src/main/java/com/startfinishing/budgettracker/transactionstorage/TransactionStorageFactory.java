package com.startfinishing.budgettracker.transactionstorage;

/** Creates {@link TransactionStore} implementations. No instances — use static methods only. */
public final class TransactionStorageFactory {

  private TransactionStorageFactory() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static TransactionStore getMemoryStorage() {
    return MemoryStorage.getInstance();
  }

  /**
   * Placeholder until {@link CSVStorage} implements {@link TransactionStore} and a fixed URI /
   * config is wired (JAV-19 / JAV-21).
   */
  public static TransactionStore getCSVStorage() {
    throw new UnsupportedOperationException(
        "CSVStorage does not implement TransactionStore yet; wire after CSV rewrite.");
  }
}
