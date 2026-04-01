package com.startfinishing.budgettracker.transactionstorage;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Persistent CSV storage for {@link Transaction} objects. */
public class CSVStorage {

  private static final String HEADER = "description,amount,category,date";

  private final String filePath;

  private CSVStorage() {
    throw new UnsupportedOperationException("Use CSVStorage(String filePath)");
  }

  public CSVStorage(String filePath) {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path must not be blank");
    }
    this.filePath = filePath;
  }

  public List<Transaction> readTransactions() {
    Path path = Path.of(filePath);
    if (!Files.exists(path)) {
      return new ArrayList<Transaction>();
    }

    try {
      List<String> lines = Files.readAllLines(path);
      List<Transaction> transactions = new ArrayList<Transaction>();

      for (int index = 0; index < lines.size(); index++) {
        String line = lines.get(index);
        if (line.isBlank()) {
          continue;
        }
        if (index == 0 && HEADER.equals(line)) {
          continue;
        }

        String[] parts = line.split(",", -1);
        if (parts.length != 4) {
          throw new IllegalArgumentException("Invalid CSV row: " + line);
        }

        transactions.add(
            new LineItem(
                parts[0],
                Double.parseDouble(parts[1]),
                TransactionCategory.valueOf(parts[2]),
                LocalDate.parse(parts[3])));
      }

      return transactions;
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read transactions from " + filePath, e);
    }
  }

  public void writeTransactions(List<? extends Transaction> transactions) {
    Path path = Path.of(filePath);
    List<String> lines = new ArrayList<String>();
    lines.add(HEADER);

    for (Transaction transaction : transactions) {
      lines.add(
          String.join(
              ",",
              transaction.getDescription(),
              Double.toString(transaction.getAmount()),
              transaction.getCategory().name(),
              transaction.getDate().toString()));
    }

    try {
      Path parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      Files.write(path, lines);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to write transactions to " + filePath, e);
    }
  }

  public String getFilePath() {
    return filePath;
  }
}
