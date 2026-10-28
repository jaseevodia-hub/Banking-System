package banking;

// ABSTRACTION: BankAccount is an abstract superclass that stores common account data
// and declares operations that each account type must implement.
// ENCAPSULATION: Private fields protect account data from direct access.
public abstract class BankAccount
{
    // Stores the account number, customer name, and account balance.
    private final String accountNumber;
    private String customerName;
    private double balance;

    // Constructor initializes the account details and validates the opening balance.
    // The opening balance must not be negative.
    public BankAccount(String accountNumber, String customerName, double openingBalance)
    {
        if (accountNumber == null || accountNumber.trim().isEmpty())
        {
            throw new IllegalArgumentException("Account number must not be empty.");
        }

        if (customerName == null || customerName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }

        if (openingBalance < 0)
        {
            throw new IllegalArgumentException(
                    "Opening balance must not be negative. Attempted: M" + String.format("%,.2f", openingBalance));
        }

        // Saves the validated account details.
        this.accountNumber = accountNumber.trim();
        this.customerName = customerName.trim();
        this.balance = openingBalance;
    }

    // ABSTRACTION: Each subclass provides its own implementation of these methods.

    // Deposits money into the account.
    // The amount must be greater than zero.
    public abstract void deposit(double amount);

    // Withdraws money according to the rules of the account type.
    // Throws an exception if the withdrawal cannot be completed.
    public abstract void withdraw(double amount) throws InsufficientFundsException;

    // Calculates the interest earned by the account.
    public abstract double calculateInterest();

    // ENCAPSULATION: Getter methods provide controlled access to private fields.

    // Returns the account number.
    public String getAccountNumber()
    {
        return accountNumber;
    }

    // Returns the customer's name.
    public String getCustomerName()
    {
        return customerName;
    }

    // Returns the current account balance.
    public double getBalance()
    {
        return balance;
    }

    // Allows subclasses to update the balance after checking their rules.
    protected void setBalance(double balance)
    {
        this.balance = balance;
    }

    // Updates the customer name after validating it.
    public void setCustomerName(String customerName)
    {
        if (customerName == null || customerName.trim().isEmpty())
        {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }

        this.customerName = customerName.trim();
    }

    // Transfers money between accounts.
    // POLYMORPHISM: Calls the appropriate overridden withdrawal and deposit methods.
    public void transferTo(BankAccount target, double amount) throws InsufficientFundsException
    {
        // Checks that a target account was provided.
        if (target == null)
        {
            throw new IllegalArgumentException("A target account is required for a transfer.");
        }

        // Prevents transferring money to the same account.
        if (target == this)
        {
            throw new IllegalArgumentException("Money cannot be transferred to the same account.");
        }

        // Ensures the transfer amount is positive.
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        // Withdraws money from the source account and deposits it into the target account.
        this.withdraw(amount);
        target.deposit(amount);
    }

    // Formats and returns information shared by every account type.
    public String getAccountInfo()
    {
        return String.format(
                "----------------------------------------%n"
                        + "Account Number : %s%n"
                        + "Customer Name  : %s%n"
                        + "Account Type   : %s%n"
                        + "Balance        : M%,.2f%n",
                accountNumber, customerName, getClass().getSimpleName(), balance);
    }
}