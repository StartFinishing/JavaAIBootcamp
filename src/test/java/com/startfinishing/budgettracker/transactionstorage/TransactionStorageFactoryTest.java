package com.startfinishing.budgettracker.transactionstorage;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  void getCSVStorageThrowsUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, TransactionStorageFactory::getCSVStorage);
  }
}
