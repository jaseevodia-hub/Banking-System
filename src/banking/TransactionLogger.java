package banking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// FINAL CLASS: TransactionLogger is declared final because it is a utility class
// that is not intended to be extended by other classes.
// FILE HANDLING: Records successful and rejected transactions in transactions.txt
// together with the date and time of each transaction.
public final class TransactionLogger
{
    // Defines the name of the file used to store transaction records.
    public static final String FILE_NAME = "transactions.txt";

    // Defines the format used to display the date and time.
    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Defines the separator used between transaction details.
    private static final String COLUMN = " | ";

    // ENCAPSULATION: The private constructor prevents objects of this utility class from being created.
    private TransactionLogger()
    {
    }

    // Appends one transaction record to transactions.txt.
    // accountNumber: the account associated with the transaction.
    // transactionType: the type of transaction, such as DEPOSIT or WITHDRAW.
    // amount: the amount involved, or null when not applicable.
    // balance: the account balance after or around the transaction, or null.
    // result: a message indicating the success or failure of the transaction.
    public static void log(String accountNumber, String transactionType,
                           Double amount, Double balance, String result)
    {
        // Creates a StringBuilder to assemble the transaction record.
        StringBuilder record = new StringBuilder();

        // Adds the current date and time to the record.
        record.append(LocalDateTime.now().format(TIMESTAMP));

        // Adds the account number or UNKNOWN if no account number is available.
        record.append(COLUMN).append(accountNumber == null ? "UNKNOWN" : accountNumber);

        // Adds the transaction type with consistent spacing.
        record.append(COLUMN).append(pad(transactionType));

        // Adds the transaction amount in the required money format.
        record.append(COLUMN).append(money(amount));

        // Adds the account balance in the required money format.
        record.append(COLUMN).append(money(balance));

        // Adds the transaction result or an empty string if no result is provided.
        record.append(COLUMN).append(result == null ? "" : result);

        // Moves the record to a new line.
        record.append(System.lineSeparator());

        try
        {
            // Creates a File object representing the transaction log file.
            File file = new File(FILE_NAME);

            // Checks whether the file is new or empty so a header can be written if needed.
            boolean firstWrite = !file.exists() || file.length() == 0;

            // Opens the file in append mode so existing transaction records are preserved.
            try (FileWriter writer = new FileWriter(file, true);
                 BufferedWriter buffer = new BufferedWriter(writer))
            {
                // Writes the column headings when the file is first created or is empty.
                if (firstWrite)
                {
                    buffer.write(String.format("%s%s%s%s%s%s%s%s%s%s%s%n",
                            "DATE/TIME", COLUMN, "ACCOUNT", COLUMN, pad("TRANSACTION TYPE"), COLUMN,
                            "AMOUNT", COLUMN, "BALANCE", COLUMN, "RESULT / MESSAGE"));
                }

                // Writes the completed transaction record to the file.
                buffer.write(record.toString());
            }
        }
        catch (IOException e)
        {
            // EXCEPTION HANDLING: Handles file-writing errors without crashing the application.
            System.out.println("WARNING: could not write to " + FILE_NAME + " (" + e.getMessage() + ")");
        }
    }

    // Formats text to a consistent width for easier reading in the log file.
    private static String pad(String text)
    {
        return String.format("%-20s", text);
    }

    // Formats monetary values with the M currency symbol.
    // Returns N/A when the value is null.
    private static String money(Double value)
    {
        return value == null ? "N/A" : String.format("M %,.2f", value);
    }
}