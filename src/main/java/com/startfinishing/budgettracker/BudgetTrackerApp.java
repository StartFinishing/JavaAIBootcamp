package com.startfinishing.budgettracker;

import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import com.startfinishing.budgettracker.transactionstorage.TransactionStorageFactory;
import com.startfinishing.budgettracker.transactionstorage.TransactionStore;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BudgetTrackerApp {

  private static final Logger logger = LoggerFactory.getLogger(BudgetTrackerApp.class);

  public static void main(String[] args) {
    BudgetTrackerApp app;
    logger.debug("Application started with arguments: {}", Arrays.toString(args));

    if (args.length < 1) {
      logger.warn("Expected at least 1 argument but received {}", args.length);
      printUsage();
      return;
    }
    logger.debug("Storage type: {}", AppProperties.getProperty("app.storage"));
    logger.debug("Command: {}", args[0]);

    String command = args[0];
    app = new BudgetTrackerApp();
    switch (command) {
      case "addTransaction":
        app.addTransaction(args);
        break;
      case "summarise":
        app.summarise();
        break;
      default:
        logger.warn("Unknown command '{}'", command);
        printUsage();
        break;
    }
  }

  private static void printUsage() {
    logger.info("Usage:");
    logger.info(
        "   addTransaction <description> <amount> <category> <date d/M/yyyy or d/M/yy>"
            + " (day/month, e.g. 12/5/2026, 15/05/2026, 12/5/26; category: enum name, case-insensitive)");
    logger.info("   summarise");
  }

  private final TransactionStore storage;

  private BudgetTrackerApp() {
    switch (AppProperties.getProperty("app.storage")) {
      case "csv":
        storage = TransactionStorageFactory.getCSVStorage();
        break;
      case "memory":
        storage = TransactionStorageFactory.getMemoryStorage();
        break;
      default:
        throw new IllegalArgumentException(
            "Invalid storage type: " + AppProperties.getProperty("app.storage"));
    }
  }

  private void addTransaction(String[] args) {
    if (args.length < 5) {
      logger.warn("Expected at least 5 arguments but received {}", args.length);
      printUsage();
      return;
    }
    try {
      Transaction transaction = storage.addTransaction(args[1], args[2], args[3], args[4]);
      logger.info("Transaction added: {}", transaction);
    } catch (IllegalArgumentException e) {
      logger.error("Error adding transaction: {}", e.getMessage());
      printUsage();
    }
  }

  private void summarise() {
    List<Transaction> transactions = storage.getTransactions();
    double total = transactions.stream().mapToDouble(Transaction::getAmount).sum();
    transactions.forEach(System.out::println);
    logger.info("Total: {}", total);
    logger.info("Average: {}", transactions.isEmpty() ? 0 : total / transactions.size());
    logger.info(
        "Min: {}", transactions.stream().mapToDouble(Transaction::getAmount).min().orElse(0));
    logger.info(
        "Max: {}", transactions.stream().mapToDouble(Transaction::getAmount).max().orElse(0));
    logger.info("Count: {}", transactions.size());

    Map<TransactionCategory, Double> categoryTotals =
        transactions.stream()
            .collect(
                Collectors.groupingBy(
                    Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
    categoryTotals.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(e -> logger.info("Category {} total: {}", e.getKey(), e.getValue()));
  }
}
