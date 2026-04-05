package com.startfinishing.budgettracker;

import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transactionstorage.TransactionStorageFactory;
import com.startfinishing.budgettracker.transactionstorage.TransactionStore;
import java.util.Arrays;
import java.util.List;
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
        "   addTransaction <description> <amount> <category> <date dd/MM/yy>"
            + " (category: enum name, case-insensitive)");
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
    for (Transaction transaction : transactions) {
      System.out.println(transaction);
    }
  }
}
