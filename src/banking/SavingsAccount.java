package banking;

// INHERITANCE: SavingsAccount extends BankAccount and reuses its fields,
// constructor, getters, and transferTo() logic.
// INTERFACE: Implements Loanable to allow savings accounts to request loans.
// METHOD OVERRIDING: Provides savings-specific rules for deposits, withdrawals,
// interest calculation, and loan requests.
public class SavingsAccount extends BankAccount implements Loanable
{
    // Defines the minimum balance required in a savings account.
    private static final double MIN_BALANCE = 100.00;

    // Defines the annual interest rate of 5%.
    private static final double ANNUAL_INTEREST_RATE = 0.05;

    // Defines the maximum loan amount for a savings account.
    private static final double LOAN_LIMIT = 50000.00;

    // Constructor that initializes a savings account.
    public SavingsAccount(String accountNumber, String customerName, double openingBalance)
    {
        // INHERITANCE: Uses the superclass constructor for validation and initialization.
        super(accountNumber, customerName, openingBalance);
    }

    // METHOD OVERRIDING: Deposits must be greater than zero.
    @Override
    public void deposit(double amount)
    {
        // Checks whether the deposit amount is valid.
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Deposit must be greater than zero. Attempted: M"
                    + String.format("%,.2f", amount));
        }

        // Adds the deposit amount to the account balance.
        setBalance(getBalance() + amount);
    }

    // METHOD OVERRIDING: Ensures the minimum savings balance of M100.00 is maintained.
    @Override
    public void withdraw(double amount) throws InsufficientFundsException
    {
        // Checks whether the withdrawal amount is valid.
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Withdrawal must be greater than zero. Attempted: M"
                    + String.format("%,.2f", amount));
        }

        // Calculates how much can be withdrawn without breaking the minimum balance rule.
        double available = getBalance() - MIN_BALANCE;

        // Checks whether the requested withdrawal exceeds the available amount.
        if (amount > available)
        {
            throw new InsufficientFundsException(String.format(
                    "Insufficient funds: a savings account must keep a minimum balance of M%,.2f. "
                            + "Available to withdraw: M%,.2f (balance M%,.2f).",
                    MIN_BALANCE, Math.max(0.0, available), getBalance()));
        }

        // Deducts the withdrawal amount from the account balance.
        setBalance(getBalance() - amount);
    }

    // METHOD OVERRIDING: Calculates 5% annual interest and adds it to the balance.
    @Override
    public double calculateInterest()
    {
        // Calculates interest using the current account balance.
        double interest = getBalance() * ANNUAL_INTEREST_RATE;

        // Credits the calculated interest to the account.
        setBalance(getBalance() + interest);

        // Returns the interest amount earned.
        return interest;
    }

    // INTERFACE IMPLEMENTATION: Processes loan requests according to savings account rules.
    @Override
    public boolean requestLoan(double amount) throws InvalidLoanException
    {
        // Checks whether the requested loan amount is positive.
        if (amount <= 0)
        {
            throw new InvalidLoanException("Loan request must be greater than zero. Requested: M"
                    + String.format("%,.2f", amount));
        }

        // Checks whether the requested loan exceeds the permitted limit.
        if (amount > LOAN_LIMIT)
        {
            throw new InvalidLoanException(String.format(
                    "Loan request of M%,.2f exceeds the savings account loan limit of M%,.2f.",
                    amount, LOAN_LIMIT));
        }

        // Credits the approved loan amount to the account balance.
        setBalance(getBalance() + amount);

        // Returns true to indicate that the loan was approved.
        return true;
    }

    // METHOD OVERRIDING: Displays the shared account information and savings-specific rules.
    @Override
    public String getAccountInfo()
    {
        // Combines the superclass account information with savings account details.
        return super.getAccountInfo() + String.format(
                "Minimum Balance: M%,.2f%n"
                        + "Interest Rate  : %.0f%% per year%n"
                        + "Loan Limit     : M%,.2f%n"
                        + "----------------------------------------%n",
                MIN_BALANCE, ANNUAL_INTEREST_RATE * 100, LOAN_LIMIT);
    }
}