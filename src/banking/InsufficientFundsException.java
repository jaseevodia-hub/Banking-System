package banking;

// EXCEPTION HANDLING: A checked business-rule exception thrown when a withdrawal
// or transfer is not supported by the available funds in the account.
// This may happen because of a savings minimum balance or current account overdraft limit.
public class InsufficientFundsException extends Exception
{
    // Identifies the version of this exception class.
    private static final long serialVersionUID = 1L;

    // Constructor that accepts an error message.
    public InsufficientFundsException(String message)
    {
        // Passes the error message to the parent Exception class.
        super(message);
    }
}