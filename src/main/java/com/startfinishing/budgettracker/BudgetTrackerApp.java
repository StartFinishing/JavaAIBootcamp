package com.startfinishing.budgettracker;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import com.startfinishing.budgettracker.transactionstorage.CSVStorage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BudgetTrackerApp {

  private static final Logger logger = LoggerFactory.getLogger(BudgetTrackerApp.class);

  private BudgetTrackerApp() {}

  public static void main(String[] args) {
    logger.debug("Application started with arguments: {}", Arrays.toString(args));

    if (args.length < 2) {
      logger.warn("Expected at least 2 arguments but received {}", args.length);
      printUsage();
      return;
    }
    logger.debug("Storage type: {}", AppProperties.getProperty("app.storage"));

    String filePath = args[0];
    String command = args[1];
    logger.debug("Parsed filePath='{}', command='{}'", filePath, command);
    CSVStorage storage = new CSVStorage(filePath);

    switch (command) {
      case "addTransaction":
        addTransaction(args, storage);
        break;
      case "summarise":
        summarise(storage);
        break;
      default:
        logger.warn("Unknown command '{}' for file '{}'", command, filePath);
        System.out.println("Unknown command: " + command);
        printUsage();
    }
  }

  private static void addTransaction(String[] args, CSVStorage storage) {
    if (args.length != 5) {
      logger.warn("Invalid addTransaction arguments: {}", Arrays.toString(args));
      System.out.println("Usage: <filePath> addTransaction <description> <amount> <category>");
      return;
    }

    String description = args[2];
    double amount = Double.parseDouble(args[3]);
    TransactionCategory category = TransactionCategory.valueOf(args[4].toUpperCase());
    logger.debug(
        "Adding transaction description='{}', amount={}, category={}, file='{}'",
        description,
        amount,
        category,
        storage.getFilePath());

    List<Transaction> transactions = storage.readTransactions();
    transactions.add(new LineItem(description, amount, category, LocalDate.now()));
    storage.writeTransactions(transactions);

    logger.info("Added transaction '{}' to '{}'", description, storage.getFilePath());
    System.out.println("Added transaction: " + description);
  }

  private static void summarise(CSVStorage storage) {
    List<Transaction> transactions = storage.readTransactions();
    double total = transactions.stream().mapToDouble(Transaction::getAmount).sum();
    logger.info(
        "Summarising {} transactions from '{}'", transactions.size(), storage.getFilePath());

    System.out.println("Transactions: " + transactions.size());
    System.out.println("Total: " + total);

    for (TransactionCategory category : TransactionCategory.values()) {
      double categoryTotal =
          transactions.stream()
              .filter(transaction -> transaction.getCategory() == category)
              .mapToDouble(Transaction::getAmount)
              .sum();
      System.out.println(category + ": " + categoryTotal);
    }
  }

  private static void printUsage() {
    logger.debug("Printing usage instructions");
    System.out.println("Usage:");
    System.out.println("  <filePath> addTransaction <description> <amount> <category>");
    System.out.println("  <filePath> summarise");
  }
}
