package com.startfinishing.budgettracker.transactionstorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * JAV-28: CSV as write-through cache — load existing file on construction, mutate memory, flush
 * after writes. New instance on the same path must see persisted data.
 */
class CSVStorageTest {

  @TempDir Path tempDir;

  @Test
  void constructorWithMissingFile_yieldsEmptyGetTransactions() {
    Path csv = tempDir.resolve("missing.csv");

    CSVStorage storage = new CSVStorage(csv.toString());

    assertTrue(storage.getTransactions().isEmpty());
  }

  @Test
  void constructorLoadsExistingCsvIntoMemory() throws Exception {
    Path csv = tempDir.resolve("seeded.csv");
    Files.writeString(
        csv,
        CSVStorage.HEADER
            + "\n"
            + "Coffee,4.5,CAFE,2026-03-31\n"
            + "Bus,25.0,TRANSPORTATION,2026-03-30\n",
        StandardCharsets.UTF_8);

    CSVStorage storage = new CSVStorage(csv.toString());

    List<Transaction> loaded = storage.getTransactions();
    assertEquals(2, loaded.size());
    assertEquals("Coffee", loaded.get(0).getDescription());
    assertEquals(4.5, loaded.get(0).getAmount(), 0.0001);
    assertEquals(TransactionCategory.CAFE, loaded.get(0).getCategory());
    assertEquals(LocalDate.of(2026, 3, 31), loaded.get(0).getDate());
    assertEquals("Bus", loaded.get(1).getDescription());
  }

  @Test
  void addTransactionFlushesToFile() throws Exception {
    Path csv = tempDir.resolve("out.csv");
    CSVStorage storage = new CSVStorage(csv.toString());
    Transaction tx = new LineItem("Tea", 1.0, TransactionCategory.CAFE, LocalDate.of(2026, 1, 15));

    storage.addTransaction(tx);

    String written = Files.readString(csv, StandardCharsets.UTF_8);
    assertTrue(written.contains(CSVStorage.HEADER));
    assertTrue(written.contains("Tea"));
    assertTrue(written.contains("1.0"));
    assertTrue(written.contains("CAFE"));
    assertTrue(written.contains("2026-01-15"));
  }

  @Test
  void secondInstanceOnSamePathReloadsFromDisk() throws Exception {
    Path csv = tempDir.resolve("shared.csv");
    CSVStorage first = new CSVStorage(csv.toString());
    first.addTransaction(
        new LineItem("Persist", 10.0, TransactionCategory.OTHER, LocalDate.of(2026, 6, 1)));

    CSVStorage second = new CSVStorage(csv.toString());

    List<Transaction> loaded = second.getTransactions();
    assertEquals(1, loaded.size());
    assertEquals("Persist", loaded.get(0).getDescription());
    assertEquals(10.0, loaded.get(0).getAmount(), 0.0001);
  }

  @Test
  void constructorRejectsBlankPath() {
    assertThrows(IllegalArgumentException.class, () -> new CSVStorage("   "));
  }

  @Test
  void constructorSkipsRowsWithWrongColumnCount() throws Exception {
    Path csv = tempDir.resolve("malformed.csv");
    Files.writeString(
        csv,
        CSVStorage.HEADER
            + "\n"
            + "Good,1.0,OTHER,2026-01-01\n"
            + "bad_row_only_three_cols,1.0,OTHER\n"
            + "AlsoGood,2.0,GROCERIES,2026-01-02\n",
        StandardCharsets.UTF_8);

    CSVStorage storage = new CSVStorage(csv.toString());

    List<Transaction> loaded = storage.getTransactions();
    assertEquals(2, loaded.size());
    assertEquals("Good", loaded.get(0).getDescription());
    assertEquals("AlsoGood", loaded.get(1).getDescription());
  }

  @Test
  void constructorSkipsRowsThatFailParsingOrValidation() throws Exception {
    Path csv = tempDir.resolve("bad_cells.csv");
    Files.writeString(
        csv,
        CSVStorage.HEADER
            + "\n"
            + "Ok,1.0,OTHER,2026-01-01\n"
            + "BadAmount,not_a_number,OTHER,2026-01-02\n"
            + "BadCategory,1.0,NO_SUCH_ENUM,2026-01-03\n"
            + "BadDate,1.0,OTHER,not-a-date\n"
            + "ZeroAmount,0,OTHER,2026-01-04\n"
            + "StillOk,2.0,GROCERIES,2026-01-05\n",
        StandardCharsets.UTF_8);

    CSVStorage storage = new CSVStorage(csv.toString());

    List<Transaction> loaded = storage.getTransactions();
    assertEquals(2, loaded.size());
    assertEquals("Ok", loaded.get(0).getDescription());
    assertEquals("StillOk", loaded.get(1).getDescription());
  }
}
