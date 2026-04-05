package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.AppProperties;

/** Creates {@link TransactionStore} implementations. No instances — use static methods only. */
public final class TransactionStorageFactory {

  private TransactionStorageFactory() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static TransactionStore getMemoryStorage() {
    return MemoryStorage.getInstance();
  }

  /**
   * New {@link CSVStorage} each call, using {@code app.storage.csv.path} from {@link
   * AppProperties}.
   */
  public static TransactionStore getCSVStorage() {
    return new CSVStorage(AppProperties.getProperty("app.storage.csv.path"));
  }
}
