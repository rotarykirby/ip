package lebron;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A task that must be completed by a specific date/time, specified after /by
 */
public class Deadline extends Task {
    protected LocalDate byDate;
    protected LocalDateTime byDateTime;
    protected boolean time;

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Creates a Deadline task due on the specified date or date-time.
     *
     * @param description description of the task.
     * @param by the due date/time.
     * @throws LebronException if time is in an invalid format
     */
    public Deadline(String description, String by) throws LebronException {
        super(description);
        parseBy(by);
    }

    /**
     * Parses the date/time the task has to be done by. 
     * Ensures that formatting is correct and can be understood by other functions in the program.
     *
     * @param by the date/time the task has to be done by.
     * @throws LebronException if empty date/time or invalid format is encountered
     */
    private void parseBy(String by) throws LebronException {
        if (by == null || by.trim().isEmpty()) {
            throw new LebronException("Deadline's date/time cannot be empty.");
        }

        by = by.trim();

        // Parse as 'yyyy-MM-dd HHmm'
        try {
            this.byDateTime = LocalDateTime.parse(by, INPUT_DATETIME);
            this.time = true;
        } catch (DateTimeParseException e1) {
            // Not a valid date-time format, try to parse as 'yyyy-MM-dd'
            try {
                this.byDate = LocalDate.parse(by, INPUT_DATE);
                this.time = false;
            } catch (DateTimeParseException e2) {
                // Not a valid date format
                throw new LebronException("Enter a valid Deadline format:\n\n" +
                        "    yyyy-MM-dd HHmm\n" +
                        "    yyyy-MM-dd");
            }
        }
    }

    /**
     * Returns the formatted deadline.
     *
     * @return the deadline string.
     */
    public String getBy() {
        if (time) {
            return byDateTime.format(OUTPUT_DATETIME);
        } else {
            return byDate.format(OUTPUT_DATE);
        }
    }

    /**
     * Returns the original formatted deadline, as entered by the user
     *
     * @return the original deadline string.
     */
    public String getOriginalBy() {
        if (time) {
            return byDateTime.format(INPUT_DATETIME);
        } else {
            return byDate.format(INPUT_DATE);
        }
    }

    /**
     * Returns true if this task occurs on or during the specified date, false otherwise.
     *
     * @param date the date.
     * @return the boolean.
     */
    public boolean isOnDate(LocalDate date) {
        return time ? byDateTime.toLocalDate().equals(date) : byDate.equals(date);
    }

    /**
     * Returns a human-readable string for display.
     *
     * @return Human-readable string.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getBy() + ")";
    }
}
