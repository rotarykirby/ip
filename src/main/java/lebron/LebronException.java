package lebron;

/**
 * Custom checked exception for errors occurring within the Lebron program.
 */
public class LebronException extends Exception {
    /**
     * Creates a custom checked exception with a message when the program throws the exception.
     *
     * @param message additional information about why the exception was thrown.
     */
    public LebronException(String message) {
        super(message);
    }
}
