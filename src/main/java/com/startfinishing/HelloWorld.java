    package com.startfinishing;

    import com.startfinishing.budgettracker.BudgetEntry;
    import com.startfinishing.budgettracker.BudgetEntryException;
    import com.startfinishing.budgettracker.TransactionCategory;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.ArrayList;


    public class HelloWorld {


    private static List<BudgetEntry> entries;

        /**
         * Static initializer block for HelloWorld.
         */
        static {
            entries = new ArrayList<BudgetEntry>();
            try {
                entries.add(new BudgetEntry("Bus pass", 25.0, TransactionCategory.TRANSPORTATION, LocalDate.now().minusDays(1)));
                entries.add(new BudgetEntry("Groceries", 100.0, TransactionCategory.GROCERIES, LocalDate.now()));
                entries.add(new BudgetEntry("Rent", 1000.0, TransactionCategory.OTHER, LocalDate.now()));
                entries.add(new BudgetEntry("Utilities", 100.0, TransactionCategory.UTILITIES, LocalDate.now()));
                entries.add(new BudgetEntry("Entertainment", 100.0, TransactionCategory.ENTERTAINMENT, LocalDate.now()));
                entries.add(new BudgetEntry("Other", 100.0, TransactionCategory.OTHER, LocalDate.now()));
                entries.add(new BudgetEntry("Food", 100.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new BudgetEntry("More Food", 100.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new BudgetEntry("More Food", 150.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new BudgetEntry("Healthcare", 100.1, TransactionCategory.HEALTHCARE, LocalDate.now()));
            } catch (BudgetEntryException e) {
                // In production: log or otherwise communicate initialization errors.
                // For this example, print (but won't propagate outside static block).
                System.out.println("Static initialization error: " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            double total = entries.stream().mapToDouble(BudgetEntry::getAmount).sum();
            System.out.println("Total: " + total);
            total = entries.stream().filter(entry -> entry.getCategory() == TransactionCategory.FOOD).mapToDouble(BudgetEntry::getAmount).sum();
            System.out.println("Total food: " + total);

       }
    }
