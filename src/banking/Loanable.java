package banking;

// INTERFACE: Loanable defines a contract for bank accounts that are allowed to borrow money.
// SavingsAccount and CurrentAccount implement this interface and provide their own lending rules.
public interface Loanable
{
    // Requests a bank loan for the account.
    // The amount must be positive and within the loan limit for the account type.
    // Returns true when the loan is approved and credited to the account.
    // Throws InvalidLoanException if the request violates a lending rule.
    boolean requestLoan(double amount) throws InvalidLoanException;
}