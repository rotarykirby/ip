package lebron;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A task that spans a date or date-time range (from ... to ...).
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    protected LocalDate fromDate;
    protected LocalDateTime fromDateTime;
    protected LocalDate toDate;
    protected LocalDateTime toDateTime;
    protected boolean hasTime;

    /**
     * Creates an event instance that occurs from/to a specified date or date-time.
     *
     * @param description description of the task.
     * @param from the date/time the event starts.
     * @param to the date/time the event ends.
     * @throws LebronException if time is in an invalid format
     */
    public Event(String description, String from, String to) throws LebronException {
        super(description);
        parseFromTo(from, to);
    }

    /**
     * Parses the date/time the event starts and ends.
     * Ensures that formatting is correct and can be understood by other functions in the program.
     *
     * @param from the date/time the event starts.
     * @param to the date/time the event ends.
     * @throws LebronException if empty date/time, event end time is before start time,
     *     or if invalid format is encountered.
     */
    private void parseFromTo(String from, String to) throws LebronException {
        if (from == null || from.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /from. Consider using deadline if no start time.");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /to. Event must end eventually!!");
        }

        from = from.trim();
        to = to.trim();

        // Parse as 'yyyy-MM-dd HHmm'
        try {
            this.fromDateTime = LocalDateTime.parse(from, INPUT_DATETIME);
            this.toDateTime = LocalDateTime.parse(to, INPUT_DATETIME);
            this.hasTime = true;
            if (fromDateTime.isAfter(toDateTime)) {
                throw new LebronException("Event start time cannot be after end time");
            }
            return;
        } catch (DateTimeParseException e1) {
            // Not a valid date-time format, try to parse as 'yyyy-MM-dd'
            try {
                this.fromDate = LocalDate.parse(from, INPUT_DATE);
                this.toDate = LocalDate.parse(to, INPUT_DATE);
                this.hasTime = false;
                if (fromDate.isAfter(toDate)) {
                    throw new LebronException("Event start date cannot be after end date");
                }
                return;
            } catch (DateTimeParseException e2) {
                // Not a valid date format
                System.out.println(from);
                throw new LebronException("Enter dates in a valid format:\n\n"
                        + "    yyyy-MM-dd HHmm\n"
                        + "    yyyy-MM-dd\n\n"
                        + "    Note that both Start and End must have the same time format.");
            }
        }
    }

    /**
     * Returns the formatted start date/time.
     *
     * @return the formatted start date/time.
     */
    public String getFrom() {
        if (hasTime) {
            return fromDateTime.format(OUTPUT_DATETIME);
        } else {
            return fromDate.format(OUTPUT_DATE);
        }
    }

    /**
     * Returns the formatted end date/time.
     *
     * @return the formatted end date/time.
     */
    public String getTo() {
        if (hasTime) {
            return toDateTime.format(OUTPUT_DATETIME);
        } else {
            return toDate.format(OUTPUT_DATE);
        }
    }

    /**
     * Returns the original start date/time string as typed by the user.
     *
     * @return the formatted start date/time.
     */
    public String getOriginalFrom() {
        if (hasTime) {
            return fromDateTime.format(INPUT_DATETIME);
        } else {
            return fromDate.format(INPUT_DATE);
        }
    }

    /**
     * Returns the original end date/time string as typed by the user.
     *
     * @return the formatted end date/time.
     */
    public String getOriginalTo() {
        if (hasTime) {
            return toDateTime.format(INPUT_DATETIME);
        } else {
            return toDate.format(INPUT_DATE);
        }
    }

    /**
     * Returns true if this event occurs on the specified date.
     *
     * @param date the date.
     * @return the boolean.
     */
    public boolean isOnDate(LocalDate date) {
        LocalDate from = hasTime ? fromDateTime.toLocalDate() : fromDate;
        LocalDate to = hasTime ? toDateTime.toLocalDate() : toDate;
        return !date.isBefore(from) && !date.isAfter(to);
    }

    /**
     * Returns a human-readable string for display.
     *
     * @return Human-readable string.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}
