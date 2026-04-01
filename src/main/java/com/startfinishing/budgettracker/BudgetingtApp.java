package com.startfinishing.budgettracker;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import com.startfinishing.budgettracker.transactionstorrage.CSVStorage;
import java.time.LocalDate;
import java.util.List;

public class BudgetingtApp {

  private BudgetingtApp() {}

  public static void main(String[] args) {
    if (args.length < 2) {
      printUsage();
      return;
    }

    String filePath = args[0];
    String command = args[1];
    CSVStorage storage = new CSVStorage(filePath);

    switch (command) {
      case "addTransaction":
        addTransaction(args, storage);
        break;
      case "summarise":
        summarise(storage);
        break;
      default:
        System.out.println("Unknown command: " + command);
        printUsage();
    }
  }

  private static void addTransaction(String[] args, CSVStorage storage) {
    if (args.length != 5) {
      System.out.println("Usage: <filePath> addTransaction <description> <amount> <category>");
      return;
    }

    String description = args[2];
    double amount = Double.parseDouble(args[3]);
    TransactionCategory category = TransactionCategory.valueOf(args[4].toUpperCase());

    List<Transaction> transactions = storage.readTransactions();
    transactions.add(new LineItem(description, amount, category, LocalDate.now()));
    storage.writeTransactions(transactions);

    System.out.println("Added transaction: " + description);
  }

  private static void summarise(CSVStorage storage) {
    List<Transaction> transactions = storage.readTransactions();
    double total = transactions.stream().mapToDouble(Transaction::getAmount).sum();

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
    System.out.println("Usage:");
    System.out.println("  <filePath> addTransaction <description> <amount> <category>");
    System.out.println("  <filePath> summarise");
  }
}
