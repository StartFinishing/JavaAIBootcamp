package com.startfinishing.budgettracker.transactionstorage;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class TransactionStorageFactoryTest {

  @Test
  void getMemoryStorageReturnsSameInstanceOnEachCall() {
    TransactionStore first = TransactionStorageFactory.getMemoryStorage();
    TransactionStore second = TransactionStorageFactory.getMemoryStorage();

    assertSame(first, second);
    assertSame(MemoryStorage.getInstance(), first);
  }

  @Test
  void getCSVStorageReturnsNewCsvStorageEachCall() {
    TransactionStore first = TransactionStorageFactory.getCSVStorage();
    TransactionStore second = TransactionStorageFactory.getCSVStorage();

    assertInstanceOf(CSVStorage.class, first);
    assertInstanceOf(CSVStorage.class, second);
    assertNotSame(first, second);
  }
}
