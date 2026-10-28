package banking;

// EXCEPTION HANDLING: A checked business-rule exception thrown when a loan
// request does not meet the bank's lending rules.
// This may happen when the requested amount is not positive or exceeds
// the permitted loan limit for the account type.
public class InvalidLoanException extends Exception
{
    // Identifies the version of this exception class.
    private static final long serialVersionUID = 1L;

    // Constructor that accepts an error message.
    public InvalidLoanException(String message)
    {
        // Passes the error message to the parent Exception class.
        super(message);
    }
}