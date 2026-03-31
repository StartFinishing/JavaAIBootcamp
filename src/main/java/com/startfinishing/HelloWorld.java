    package com.startfinishing;

    import com.startfinishing.budgettracker.LineItem;
    import com.startfinishing.budgettracker.Transaction;
    import com.startfinishing.budgettracker.TransactionCategory;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.ArrayList;


    public class HelloWorld {


    private static List<Transaction> entries;

        /**
         * Static initializer block for HelloWorld.
         */
        static {
            entries = new ArrayList<Transaction>();
            try {
                entries.add(new LineItem("Bus pass", 25.0, TransactionCategory.TRANSPORTATION, LocalDate.now().minusDays(1)));
                entries.add(new LineItem("Groceries", 100.0, TransactionCategory.GROCERIES, LocalDate.now()));
                entries.add(new LineItem("Rent", 1000.0, TransactionCategory.OTHER, LocalDate.now()));
                entries.add(new LineItem("Utilities", 100.0, TransactionCategory.UTILITIES, LocalDate.now()));
                entries.add(new LineItem("Entertainment", 100.0, TransactionCategory.ENTERTAINMENT, LocalDate.now()));
                entries.add(new LineItem("Other", 100.0, TransactionCategory.OTHER, LocalDate.now()));
                entries.add(new LineItem("Food", 100.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new LineItem("More Food", 100.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new LineItem("More Food", 150.0, TransactionCategory.FOOD, LocalDate.now()));
                entries.add(new LineItem("Healthcare", 100.1, TransactionCategory.HEALTHCARE, LocalDate.now()));
            } catch (IllegalArgumentException e) {
                // In production: log or otherwise communicate initialization errors.
                // For this example, print (but won't propagate outside static block).
                System.out.println("Static initialization error: " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            double total = entries.stream().mapToDouble(Transaction::getAmount).sum();
            System.out.println("Total: " + total);
            total = entries.stream().filter(entry -> entry.getCategory() == TransactionCategory.FOOD).mapToDouble(Transaction::getAmount).sum();
            System.out.println("Total food: " + total);

       }
    }
