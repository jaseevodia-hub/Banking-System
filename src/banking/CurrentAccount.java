package banking;

// INHERITANCE: CurrentAccount extends BankAccount.
// INTERFACE: Implements Loanable to allow eligible current accounts to request loans.
// METHOD OVERRIDING: Provides current-account rules for deposits, withdrawals, interest, and loans.
public class CurrentAccount extends BankAccount implements Loanable
{
    // Defines the maximum overdraft and loan amounts.
    private static final double OVERDRAFT_LIMIT = 2000.00;
    private static final double LOAN_LIMIT = 30000.00;

    // Constructor initializes the current account using the superclass constructor.
    public CurrentAccount(String accountNumber, String customerName, double openingBalance)
    {
        // INHERITANCE: Common validation and initialization come from the superclass.
        super(accountNumber, customerName, openingBalance);
    }

    // OVERRIDING: Deposits must be greater than zero.
    @Override
    public void deposit(double amount)
    {
        // Validates the deposit amount.
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Deposit must be greater than zero. Attempted: M"
                    + String.format("%,.2f", amount));
        }

        // Adds the deposit to the current balance.
        setBalance(getBalance() + amount);
    }

    // OVERRIDING: Allows withdrawals within the overdraft limit of M2,000.00.
    @Override
    public void withdraw(double amount) throws InsufficientFundsException
    {
        // Ensures the withdrawal amount is greater than zero.
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Withdrawal must be greater than zero. Attempted: M"
                    + String.format("%,.2f", amount));
        }

        // Checks whether the withdrawal would exceed the overdraft limit.
        if (getBalance() - amount < -OVERDRAFT_LIMIT)
        {
            throw new InsufficientFundsException(String.format(
                    "Insufficient funds: the overdraft limit is M%,.2f. "
                            + "Available to withdraw: M%,.2f (balance M%,.2f).",
                    OVERDRAFT_LIMIT, getBalance() + OVERDRAFT_LIMIT, getBalance()));
        }

        // Deducts the withdrawal amount from the balance.
        setBalance(getBalance() - amount);
    }

    // OVERRIDING: Current accounts do not earn interest.
    @Override
    public double calculateInterest()
    {
        // Returns zero because no interest is earned.
        return 0.0;
    }

    // INTERFACE IMPLEMENTATION: Processes loan requests within the loan limit.
    @Override
    public boolean requestLoan(double amount) throws InvalidLoanException
    {
        // Rejects loan amounts that are not greater than zero.
        if (amount <= 0)
        {
            throw new InvalidLoanException("Loan request must be greater than zero. Requested: M"
                    + String.format("%,.2f", amount));
        }

        // Rejects loan requests that exceed the maximum loan limit.
        if (amount > LOAN_LIMIT)
        {
            throw new InvalidLoanException(String.format(
                    "Loan request of M%,.2f exceeds the current account loan limit of M%,.2f.",
                    amount, LOAN_LIMIT));
        }

        // Adds the approved loan amount to the account balance.
        setBalance(getBalance() + amount);
        return true;
    }

    // OVERRIDING: Adds current-account information to the shared account details.
    @Override
    public String getAccountInfo()
    {
        // Combines superclass account information with current-account limits and interest details.
        return super.getAccountInfo() + String.format(
                "Overdraft Limit: M%,.2f%n"
                        + "Interest Rate  : 0%% (current accounts earn no interest)%n"
                        + "Loan Limit     : M%,.2f%n"
                        + "----------------------------------------%n",
                OVERDRAFT_LIMIT, LOAN_LIMIT);
    }
}