package com.startfinishing.budgettracker.transactionstorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CSVStorageTest {

  @TempDir Path tempDir;

  @Test
  void writeTransactionsThenReadTransactionsReturnsPersistedValues() {
    Path csvFile = tempDir.resolve("transactions.csv");
    CSVStorage storage = new CSVStorage(csvFile.toString());
    List<Transaction> transactions =
        List.of(
            new LineItem("Coffee", 4.50, TransactionCategory.FOOD, LocalDate.of(2026, 3, 31)),
            new LineItem(
                "Bus pass", 25.00, TransactionCategory.TRANSPORTATION, LocalDate.of(2026, 3, 30)));

    storage.writeTransactions(transactions);

    List<Transaction> loadedTransactions = storage.readTransactions();

    assertEquals(2, loadedTransactions.size());
    assertEquals("Coffee", loadedTransactions.get(0).getDescription());
    assertEquals(4.50, loadedTransactions.get(0).getAmount(), 0.0001);
    assertEquals(TransactionCategory.FOOD, loadedTransactions.get(0).getCategory());
    assertEquals(LocalDate.of(2026, 3, 31), loadedTransactions.get(0).getDate());
    assertEquals("Bus pass", loadedTransactions.get(1).getDescription());
  }

  @Test
  void readTransactionsReturnsEmptyListWhenFileDoesNotExist() {
    Path csvFile = tempDir.resolve("missing.csv");
    CSVStorage storage = new CSVStorage(csvFile.toString());

    List<Transaction> loadedTransactions = storage.readTransactions();

    assertEquals(0, loadedTransactions.size());
  }
}
