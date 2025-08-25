import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    protected LocalDate byDate;
    protected LocalDateTime byDateTime;
    protected boolean time;

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Deadline(String description, String by) throws LebronException {
        super(description);
        parseBy(by);
    }

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

    public String getBy() {
        if (time) {
            return byDateTime.format(OUTPUT_DATETIME);
        } else {
            return byDate.format(OUTPUT_DATE);
        }
    }

    public String getOriginalBy() {
        if (time) {
            return byDateTime.format(INPUT_DATETIME);
        } else {
            return byDate.format(INPUT_DATE);
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getBy() + ")";
    }
}
