package com.startfinishing.budgettracker;

/**
 * A generic exception type for budget entry errors.
 * 
 * Why use a custom exception?
 * - Improves clarity in exception handling by distinguishing domain errors (budget logic) from generic Java exceptions.
 * - Enables callers to catch budget-related issues more specifically, instead of catching RuntimeException or Exception.
 * - Aligns with good C practice: use domain-specific error codes/types rather than generic error signals.
 * 
 * Design Considerations:
 * - Extends RuntimeException so it is unchecked (can propagate up stack if not caught).
 * - Inclusive of various budget entry issues (validation, calculation, etc.).
 * - Provides constructors for error message and/or cause chaining.
 * 
 * Alternatives:
 * - Checked exception (extends Exception): Forces explicit handling but can be overly verbose for many app types.
 * - Using IllegalArgumentException: Less explicit, mixes app and core Java semantics.
 */
public class BudgetEntryException extends RuntimeException {

    /**
     * Constructs a new BudgetEntryException with no detail message.
     */
    public BudgetEntryException() {
        super();
    }

    /**
     * Constructs a new BudgetEntryException with the specified detail message.
     * @param message the detail message.
     */
    public BudgetEntryException(String message) {
        super(message);
    }

    /**
     * Constructs a new BudgetEntryException with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause (can be retrieved later by getCause()).
     */
    public BudgetEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new BudgetEntryException with the specified cause.
     * @param cause the cause (can be retrieved later by getCause()).
     */
    public BudgetEntryException(Throwable cause) {
        super(cause);
    }
}