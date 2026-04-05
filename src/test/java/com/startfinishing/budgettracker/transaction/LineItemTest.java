package com.startfinishing.budgettracker.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LineItemTest {

  @Test
  void constructorStoresFieldsWhenAmountPositive() {
    LocalDate date = LocalDate.of(2026, 3, 30);
    LineItem entry = new LineItem("Coffee", 4.50, TransactionCategory.CAFE, date);

    assertEquals("Coffee", entry.getDescription());
    assertEquals(4.50, entry.getAmount(), 0.0001);
    assertEquals(TransactionCategory.CAFE, entry.getCategory());
    assertEquals(date, entry.getDate());
    assertTrue(entry instanceof Transaction);
  }

  @Test
  void constructorStoresFieldsWhenDescriptionContainsLettersDigitsAndSpaces() {
    LocalDate date = LocalDate.of(2026, 3, 30);
    LineItem entry =
        new LineItem("Frederik123 Transport", 40.00, TransactionCategory.TRANSPORTATION, date);

    assertEquals("Frederik123 Transport", entry.getDescription());
  }
}
