// Imports the banking classes used in the program.
import banking.BankAccount;
import banking.CurrentAccount;
import banking.InsufficientFundsException;
import banking.InvalidLoanException;
import banking.Loanable;
import banking.SavingsAccount;
import banking.TransactionLogger;

// Imports Java utilities for lists, input, and exception handling.
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


// Main class that runs the console banking system.
public class BankingSystemMain
{

    // Reads input entered by the user.
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final List<BankAccount> ACCOUNTS = new ArrayList<>();

    // Main method: the starting point of the program.
    public static void main(String[] args)
    {
        openDemoAccounts();
        System.out.println("Welcome to the Console Banking System");

        // Controls whether the banking menu continues running.
        boolean running = true;
        while (running) {
            showMenu();
            try
            {
                int choice = readMenuChoice();

                // Executes the operation selected by the user.
                switch (choice)
                {
                    case 1:
                        displayAllAccounts();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        withdrawMoney();
                        break;
                    case 4:
                        transferMoney();
                        break;
                    case 5:
                        calculateInterest();
                        break;
                    case 6:
                        requestLoan();
                        break;
                    case 7:
                        displayAccountDetails();
                        break;
                    case 8:
                        running = false;
                        break;
                    default:
                        // EXCEPTION HANDLING: invalid menu choice
                        System.out.println("Invalid menu choice '" + choice
                                + "'. Please enter a number between 1 and 8.");
                }
            } catch (NumberFormatException e)
            {
                // Handles input that is not a valid number.
                System.out.println("Invalid numeric input: " + e.getMessage());
            } catch (NoSuchElementException e)
            {
                // Handles situations where no more input is available.
                System.out.println("\nNo more input available. Exiting safely.");
                running = false;
            } catch (Exception e)
            {
                // Handles unexpected errors to prevent the program from stopping abruptly.
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        // Closes the input scanner when the program ends.
        SCANNER.close();
        System.out.println("Session ended safely. Thank you for banking with us. Goodbye!");
    }

    // ------------------------------------------------------------------ menu

    // Displays the available banking operations.
    private static void showMenu()
    {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("            CONSOLE BANKING SYSTEM");
        System.out.println("==============================================");
        System.out.println("1. Display all accounts");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Calculate interest");
        System.out.println("6. Request bank loan");
        System.out.println("7. Display account details");
        System.out.println("8. Exit");
        System.out.println("==============================================");
    }

    // Reads and validates the user's menu selection.
    private static int readMenuChoice() {
        System.out.print("Enter your choice (1-8): ");
        String raw = SCANNER.nextLine().trim();
        if (raw.isEmpty())
        {
            throw new NumberFormatException("the menu choice is empty - please enter a number from 1 to 8");
        }
        try
        {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("'" + raw + "' is not a whole menu number - enter 1 to 8");
        }
    }

    private static double readAmount(String prompt)
    {
        System.out.print(prompt);

        // Removes the currency symbol, commas, and spaces from the input.
        String raw = SCANNER.nextLine().trim()
                .replace("M", "").replace("m", "")
                .replace(",", "").replace(" ", "");
        if (raw.isEmpty())
        {
            throw new NumberFormatException("the amount is empty - please enter a number");
        }
        double value;
        try
        {
            // Converts the cleaned input into a numeric amount.
            value = Double.parseDouble(raw);
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("'" + raw + "' is not a valid amount of money");
        }

        // Rejects infinite and NaN values.
        if (!Double.isFinite(value)) {
            throw new NumberFormatException("'" + raw + "' is not a valid amount of money");
        }
        return value;
    }

    private static BankAccount findAccount(String prompt)
    {
        System.out.print(prompt);
        String number = SCANNER.nextLine().trim();

        // Searches the list for the account matching the entered number.
        for (BankAccount account : ACCOUNTS) { // POLYMORPHISM: iterating BankAccount references
            if (account.getAccountNumber().equalsIgnoreCase(number))
            {
                return account;
            }
        }

        // Reports an error when the account does not exist.
        throw new IllegalArgumentException("no account with number '" + number + "' exists");
    }

    //Records a rejected transaction; nothing is logged when no account was found.
    private static void logFailure(String transactionType, BankAccount account, Double amount, Exception cause) {
        if (account == null) {
            return;
        }

        // Records the failed transaction and the account's current balance.
        TransactionLogger.log(account.getAccountNumber(), transactionType, amount,
                account.getBalance(), "FAILED: " + cause.getMessage());
    }

    // ----------------------------------------------------------- operations

    // POLYMORPHISM: creates subclass objects through BankAccount references
    private static void openDemoAccounts()
    {

        // Creates sample savings and current accounts.
        BankAccount savingsOne = new SavingsAccount("1001", "Neo Molefe", 5000.00);
        BankAccount savingsTwo = new SavingsAccount("1002", "Thabo Sithole", 1500.00);
        BankAccount currentOne = new CurrentAccount("2001", "Kabelo Traders", 3000.00);
        BankAccount currentTwo = new CurrentAccount("2002", "Mpho Retail", 500.00);

        // Adds the created accounts to the account list.
        ACCOUNTS.add(savingsOne);
        ACCOUNTS.add(savingsTwo);
        ACCOUNTS.add(currentOne);
        ACCOUNTS.add(currentTwo);

        System.out.println("Demo accounts opened:");

        // Displays the account numbers, customer names, and account types.
        for (BankAccount account : ACCOUNTS)
        { // POLYMORPHISM: overridden getAccountInfo() is used
            System.out.println("  " + account.getAccountNumber() + " - " + account.getCustomerName()
                    + " (" + account.getClass().getSimpleName() + ")");
        }
    }

    // Displays information about all registered accounts.
    private static void displayAllAccounts()
    {
        System.out.println();
        System.out.println("================= ALL ACCOUNTS =================");

        // Checks whether the account list is empty.
        if (ACCOUNTS.isEmpty())
        {
            System.out.println("No accounts are registered.");
            return;
        }

        // POLYMORPHISM: the same call reaches SavingsAccount or CurrentAccount code
        for (BankAccount account : ACCOUNTS)
        {
            System.out.print(account.getAccountInfo());
        }

        // Displays the total number of registered accounts.
        System.out.println("Total accounts: " + ACCOUNTS.size());
    }

    // Displays details of one selected account.
    private static void displayAccountDetails()
    {
        BankAccount account = null;
        try
        {
            account = findAccount("Enter account number: ");
            System.out.println();
            System.out.println("------------- ACCOUNT DETAILS --------------");

            // POLYMORPHISM: getAccountInfo() is overridden in each subclass
            System.out.print(account.getAccountInfo());
        } catch (IllegalArgumentException e)
        {
            // Displays an error if the account cannot be found.
            System.out.println("Cannot display account: " + e.getMessage());
        }
    }

    // Handles deposits into a bank account.
    private static void depositMoney()
    {
        BankAccount account = null;
        Double amount = null;
        try
        {
            account = findAccount("Enter account number to deposit into: ");
            amount = readAmount("Enter amount to deposit (M): ");

            // POLYMORPHISM + OVERRIDING: rule checked by the subclass
            account.deposit(amount);

            // Records a successful deposit.
            TransactionLogger.log(account.getAccountNumber(), "DEPOSIT", amount,
                    account.getBalance(), "SUCCESS: deposit accepted");
            System.out.printf("Deposited M%,.2f. New balance: M%,.2f%n",
                    amount, account.getBalance());
        } catch (NumberFormatException e)
        {
            // Handles invalid deposit amounts.
            System.out.println("Invalid numeric input: " + e.getMessage());
            logFailure("DEPOSIT", account, amount, e);
        } catch (IllegalArgumentException e)
        {
            // Handles deposits rejected by the account's validation rules.
            System.out.println("Deposit rejected: " + e.getMessage());
            logFailure("DEPOSIT", account, amount, e);
        }
    }

    // Handles withdrawals from a bank account.
    private static void withdrawMoney() {
        BankAccount account = null;
        Double amount = null;
        try {
            account = findAccount("Enter account number to withdraw from: ");
            amount = readAmount("Enter amount to withdraw (M): ");

            // OVERRIDING: savings minimum or overdraft rule applies
            account.withdraw(amount);

            // Records a successful withdrawal.
            TransactionLogger.log(account.getAccountNumber(), "WITHDRAW", amount,
                    account.getBalance(), "SUCCESS: withdrawal accepted");
            System.out.printf("Withdrew M%,.2f. New balance: M%,.2f%n",
                    amount, account.getBalance());
        } catch (NumberFormatException e)
        {
            // Handles invalid withdrawal amounts.
            System.out.println("Invalid numeric input: " + e.getMessage());
            logFailure("WITHDRAW", account, amount, e);
        } catch (InsufficientFundsException e)
        {
            // Handles withdrawals when there are insufficient funds.
            System.out.println("Withdrawal rejected: " + e.getMessage());
            logFailure("WITHDRAW", account, amount, e);
        } catch (IllegalArgumentException e)
        {
            // Handles other invalid withdrawal requests.
            System.out.println("Withdrawal rejected: " + e.getMessage());
            logFailure("WITHDRAW", account, amount, e);
        }
    }

    // Transfers money from one account to another.
    private static void transferMoney()
    {
        BankAccount source = null;
        BankAccount target = null;
        Double amount = null;
        try
        {
            source = findAccount("Enter YOUR account number: ");
            target = findAccount("Enter TARGET account number: ");
            amount = readAmount("Enter amount to transfer (M): ");

            // Shared method, subclass rules via polymorphism.
            source.transferTo(target, amount);

            // Records the outgoing transfer from the source account.
            TransactionLogger.log(source.getAccountNumber(), "TRANSFER OUT -> " + target.getAccountNumber(),
                    amount, source.getBalance(), "SUCCESS: money sent");

            // Records the incoming transfer to the target account.
            TransactionLogger.log(target.getAccountNumber(), "TRANSFER IN <- " + source.getAccountNumber(),
                    amount, target.getBalance(), "SUCCESS: money received");
            System.out.printf("Transferred M%,.2f from %s to %s.%n",
                    amount, source.getAccountNumber(), target.getAccountNumber());
            System.out.printf("  %s balance: M%,.2f | %s balance: M%,.2f%n",
                    source.getAccountNumber(), source.getBalance(),
                    target.getAccountNumber(), target.getBalance());
        } catch (NumberFormatException e)
        {
            // Handles invalid transfer amounts.
            System.out.println("Invalid numeric input: " + e.getMessage());
            logFailure("TRANSFER OUT", source, amount, e);
        } catch (InsufficientFundsException e)
        {
            // Handles transfers that cannot be completed due to insufficient funds.
            System.out.println("Transfer rejected: " + e.getMessage());
            logFailure("TRANSFER OUT", source, amount, e);
        } catch (IllegalArgumentException e) {
            // Handles other invalid transfer requests.
            System.out.println("Transfer rejected: " + e.getMessage());
            logFailure("TRANSFER OUT", source, amount, e);
        }
    }

    // Calculates interest according to the account type.
    private static void calculateInterest()
    {
        BankAccount account = null;
        try {
            account = findAccount("Enter account number: ");

            // POLYMORPHISM: the interest rules of the real object are executed
            double interest = account.calculateInterest();
            if (interest > 0)
            {

                // Records the interest credited to the account.
                TransactionLogger.log(account.getAccountNumber(), "INTEREST", interest,
                        account.getBalance(), "SUCCESS: annual interest credited");
                System.out.printf("Interest of M%,.2f credited (5%% per year). New balance: M%,.2f%n",
                        interest, account.getBalance());
            } else
            {
                // Records that no interest was earned by the current account.
                TransactionLogger.log(account.getAccountNumber(), "INTEREST", 0.0,
                        account.getBalance(), "NO INTEREST: current accounts earn no interest");
                System.out.println("This account earns no interest (current accounts earn 0%). "
                        + "Balance remains M" + String.format("%,.2f", account.getBalance()) + ".");
            }
        } catch (IllegalArgumentException e)
        {
            // Handles requests involving an unknown or invalid account.
            System.out.println("Interest not calculated: " + e.getMessage());
            logFailure("INTEREST", account, null, e);
        }
    }

    // Handles loan requests from eligible account types.
    private static void requestLoan()
    {
        BankAccount account = null;
        Double amount = null;
        try
        {
            account = findAccount("Enter account number for the loan request: ");
            amount = readAmount("Enter loan amount (M): ");

            // INTERFACE: the loan is requested through a Loanable reference
            if (account instanceof Loanable)
            {

                // Casts the account to Loanable so its loan method can be called.
                Loanable borrower = (Loanable) account;
                boolean approved = borrower.requestLoan(amount);
                if (approved)
                {

                    // Records the approved loan.
                    TransactionLogger.log(account.getAccountNumber(), "LOAN", amount,
                            account.getBalance(), "SUCCESS: loan approved and credited");
                    System.out.printf("Loan of M%,.2f approved and credited. New balance: M%,.2f%n",
                            amount, account.getBalance());
                }
            } else
            {
                // Rejects loan requests from account types that do not support loans.
                System.out.println("This account type is not allowed to request loans.");
                logFailure("LOAN", account, amount,
                        new IllegalArgumentException("account type does not implement Loanable"));
            }
        } catch (NumberFormatException e)
        {
            // Handles invalid loan amounts.
            System.out.println("Invalid numeric input: " + e.getMessage());
            logFailure("LOAN", account, amount, e);
        } catch (InvalidLoanException e)
        {
            // EXCEPTION HANDLING: loan rule broken (not positive or above the limit)
            System.out.println("Loan rejected: " + e.getMessage());
            logFailure("LOAN", account, amount, e);
        } catch (IllegalArgumentException e)
        {
            // Handles other invalid loan requests.
            System.out.println("Loan rejected: " + e.getMessage());
            logFailure("LOAN", account, amount, e);
        }
    }
}