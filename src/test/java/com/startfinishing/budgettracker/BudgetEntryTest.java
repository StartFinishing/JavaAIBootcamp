package com.startfinishing.budgettracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;    

class BudgetEntryTest {

    @Test
    void constructorStoresFieldsWhenAmountPositive() {
        LocalDate date = LocalDate.of(2026, 3, 30);
        BudgetEntry entry = new BudgetEntry("Coffee", 4.50, TransactionCategory.FOOD, date);

        assertEquals("Coffee", entry.getDescription());
        assertEquals(4.50, entry.getAmount(), 0.0001);
        assertEquals(TransactionCategory.FOOD, entry.getCategory());
        assertEquals(date, entry.getDate());
    }


    @Test
    void constructorThrowsExceptionWhenAmountIsNegative() {
        LocalDate date = LocalDate.of(2026, 3, 30);
        assertThrows(BudgetEntryException.class, () -> new BudgetEntry("Coffee", -4.50, TransactionCategory.FOOD, date));
    }

    @Test
    void AmountSetterThrowsExceptionWhenAmountIsNegative() {
        LocalDate date = LocalDate.of(2026, 3, 30);
        BudgetEntry entry = new BudgetEntry("Coffee", 1.00, TransactionCategory.FOOD, date);
        assertThrows(BudgetEntryException.class, () -> entry.setAmount(-1.00));
    }

}
