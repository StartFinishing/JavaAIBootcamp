package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Write-through persistence: in-memory state ({@link MemoryStorage}) is the working set; the CSV
 * file is loaded on construction (if present) and rewritten after each mutating operation (JAV-28).
 */
public class CSVStorage implements TransactionStore {

  static final String HEADER = "description,amount,category,date";

  private final Path path;
  private final MemoryStorage memory = new MemoryStorage();

  public CSVStorage(String filePath) {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path must not be blank");
    }
    this.path = Path.of(filePath);
    loadFromDiskIfPresent();
  }

  private void loadFromDiskIfPresent() {
    if (!Files.exists(path)) {
      return;
    }
    try {
      List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      if (lines.isEmpty()) {
        return;
      }
      if (!lines.get(0).trim().equals(HEADER)) {
        throw new IllegalArgumentException("CSV must start with header line: " + HEADER);
      }
      lines.stream()
          .skip(1)
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .forEach(this::parseAndAddLine);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void parseAndAddLine(String line) {
    try {
      String[] parts = line.split(",", -1);
      if (parts.length != 4) {
        return;
      }
      String description = parts[0].trim();
      double amount = Double.parseDouble(parts[1].trim());
      TransactionCategory category = TransactionCategory.valueOf(parts[2].trim());
      LocalDate date = LocalDate.parse(parts[3].trim());
      memory.addTransaction(new LineItem(description, amount, category, date));
    } catch (RuntimeException e) {
      return;
    }
  }

  private void flushToDisk() {
    try {
      Path parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      List<String> lines = new ArrayList<>();
      lines.add(HEADER);
      for (Transaction t : memory.getTransactions()) {
        lines.add(
            String.format(
                Locale.ROOT,
                "%s,%s,%s,%s",
                t.getDescription(),
                Double.toString(t.getAmount()),
                t.getCategory().name(),
                t.getDate().toString()));
      }
      Files.writeString(path, String.join("\n", lines), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void addTransaction(Transaction transaction) {
    memory.addTransaction(transaction);
    flushToDisk();
  }

  @Override
  public Transaction addTransaction(
      String description, String amountString, String category, String dateString)
      throws IllegalArgumentException, NumberFormatException, DateTimeParseException {
    Transaction added = memory.addTransaction(description, amountString, category, dateString);
    flushToDisk();
    return added;
  }

  @Override
  public void addTransaction(List<? extends Transaction> transactions) {
    memory.addTransaction(transactions);
    flushToDisk();
  }

  @Override
  public List<Transaction> getTransactions() {
    return memory.getTransactions();
  }
}
