package lebron;

/**
 * Custom checked exception for errors occurring within the Lebron program.
 */
public class LebronException extends Exception {
    public LebronException(String message) {
        super(message);
    }
}
