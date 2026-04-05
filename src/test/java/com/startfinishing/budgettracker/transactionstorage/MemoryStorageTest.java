package com.startfinishing.budgettracker.transactionstorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.startfinishing.budgettracker.transaction.LineItem;
import com.startfinishing.budgettracker.transaction.Transaction;
import com.startfinishing.budgettracker.transaction.TransactionCategory;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MemoryStorageTest {

  @Test
  void addTransactionStringAddsTransactionAndPersistsIt() {
    TransactionStore storage = new MemoryStorage();

    Transaction added = storage.addTransaction("Coffee", "4.50", "CAFE", "31/03/26");

    assertEquals("Coffee", added.getDescription());
    assertEquals(4.50, added.getAmount(), 0.0001);
    assertEquals(TransactionCategory.CAFE, added.getCategory());
    assertEquals(LocalDate.of(2026, 3, 31), added.getDate());

    List<Transaction> all = storage.getTransactions();
    assertEquals(1, all.size());
    assertEquals("Coffee", all.get(0).getDescription());
  }

  @Test
  void addTransactionStringAcceptsLowercaseCategory() {
    TransactionStore storage = new MemoryStorage();

    storage.addTransaction("Tea", "1.0", "cafe", "01/01/26");

    assertEquals(1, storage.getTransactions().size());
    assertEquals(TransactionCategory.CAFE, storage.getTransactions().get(0).getCategory());
  }

  @Test
  void addTransactionStringAcceptsFlexibleEuropeanDateFormats() {
    TransactionStore storage = new MemoryStorage();

    storage.addTransaction("A", "1.0", "OTHER", "12/5/2026");
    storage.addTransaction("B", "1.0", "OTHER", "12/05/2025");
    storage.addTransaction("C", "1.0", "OTHER", "12/5/26");
    storage.addTransaction("D", "1.0", "OTHER", "15/05/2026");

    assertEquals(4, storage.getTransactions().size());
  }

  @Test
  void addTransactionWithLineItemAddsAndPersists() {
    TransactionStore storage = new MemoryStorage();
    Transaction lineItem =
        new LineItem("Bus", 25.0, TransactionCategory.TRANSPORTATION, LocalDate.of(2026, 1, 15));

    storage.addTransaction(lineItem);

    assertEquals(1, storage.getTransactions().size());
    assertEquals("Bus", storage.getTransactions().get(0).getDescription());
  }

  @Test
  void addTransactionListAddsAllValidTransactions() {
    TransactionStore storage = new MemoryStorage();
    List<Transaction> batch =
        List.of(
            new LineItem("A", 1.0, TransactionCategory.OTHER, LocalDate.of(2026, 1, 1)),
            new LineItem("B", 2.0, TransactionCategory.GROCERIES, LocalDate.of(2026, 1, 2)));

    storage.addTransaction(batch);

    assertEquals(2, storage.getTransactions().size());
  }

  @Test
  void addTransactionListEmptyDoesNotThrow() {
    TransactionStore storage = new MemoryStorage();

    storage.addTransaction(List.of());

    assertTrue(storage.getTransactions().isEmpty());
  }

  @Test
  void addTransactionListThrowsWhenListIsNull() {
    TransactionStore storage = new MemoryStorage();

    assertThrows(
        IllegalArgumentException.class,
        () -> storage.addTransaction((List<? extends Transaction>) null));
  }

  @Test
  void addTransactionListThrowsWhenElementIsNull() {
    TransactionStore storage = new MemoryStorage();
    List<Transaction> withNull = new ArrayList<>();
    withNull.add(new LineItem("Ok", 1.0, TransactionCategory.OTHER, LocalDate.of(2026, 1, 1)));
    withNull.add(null);

    assertThrows(IllegalArgumentException.class, () -> storage.addTransaction(withNull));
    assertTrue(storage.getTransactions().isEmpty());
  }

  @Test
  void addTransactionListThrowsWhenLaterElementInvalidAndDoesNotPartiallyAdd() {
    TransactionStore storage = new MemoryStorage();
    List<Transaction> batch = new ArrayList<>();
    batch.add(new LineItem("Ok", 1.0, TransactionCategory.OTHER, LocalDate.of(2026, 1, 1)));
    batch.add(new LineItem("Bad", 0.0, TransactionCategory.OTHER, LocalDate.of(2026, 1, 2)));

    assertThrows(IllegalArgumentException.class, () -> storage.addTransaction(batch));
    assertTrue(storage.getTransactions().isEmpty());
  }

  @Test
  void addTransactionObjectThrowsWhenLineItemAmountIsZero() {
    TransactionStore storage = new MemoryStorage();
    Transaction invalid =
        new LineItem("X", 0.0, TransactionCategory.OTHER, LocalDate.of(2026, 1, 1));

    assertThrows(IllegalArgumentException.class, () -> storage.addTransaction(invalid));
  }

  @Test
  void getTransactionsReturnsCopySoClearingItDoesNotEmptyStore() {
    TransactionStore storage = new MemoryStorage();
    storage.addTransaction("Coffee", "1.0", "OTHER", "01/01/26");

    List<Transaction> copy = storage.getTransactions();
    copy.clear();

    assertEquals(1, storage.getTransactions().size());
  }

  @Test
  void addTransactionThrowsWhenTransactionReferenceIsNull() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(IllegalArgumentException.class, () -> storage.addTransaction((Transaction) null));
  }

  @Test
  void addTransactionStringThrowsWhenAmountIsZero() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        IllegalArgumentException.class,
        () -> storage.addTransaction("Coffee", "0", "OTHER", "01/01/26"));
  }

  @Test
  void addTransactionStringThrowsWhenAmountIsNegative() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        IllegalArgumentException.class,
        () -> storage.addTransaction("Coffee", "-1.0", "OTHER", "01/01/26"));
  }

  @Test
  void addTransactionStringThrowsWhenDescriptionIsEmpty() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        IllegalArgumentException.class,
        () -> storage.addTransaction("", "1.0", "OTHER", "01/01/26"));
  }

  @Test
  void addTransactionStringThrowsWhenDateCannotBeParsed() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        DateTimeParseException.class,
        () -> storage.addTransaction("Coffee", "1.0", "OTHER", "not-a-date"));
  }

  @Test
  void addTransactionStringThrowsWhenAmountIsNotANumber() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        NumberFormatException.class,
        () -> storage.addTransaction("Coffee", "abc", "OTHER", "01/01/26"));
  }

  @Test
  void addTransactionStringThrowsWhenCategoryIsInvalid() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        IllegalArgumentException.class,
        () -> storage.addTransaction("Coffee", "1.0", "NOT_A_REAL_CATEGORY", "01/01/26"));
  }

  @Test
  void addTransactionStringThrowsWhenCategoryIsNull() {
    TransactionStore storage = new MemoryStorage();
    assertThrows(
        NullPointerException.class,
        () -> storage.addTransaction("Coffee", "1.0", null, "01/01/26"));
  }
}
