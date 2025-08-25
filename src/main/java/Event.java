import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected LocalDate fromDate;
    protected LocalDateTime fromDateTime;
    protected LocalDate toDate;
    protected LocalDateTime toDateTime;
    protected boolean fromTime;
    protected boolean toTime;

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Event(String description, String from, String to) throws LebronException {
        super(description);
        parseFrom(from);
        parseTo(to);
    }

    private void parseFrom(String from) throws LebronException {
        if (from == null || from.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /from. Consider using deadline if no start time");
        }

        from = from.trim();

        // Parse as 'yyyy-MM-dd HHmm'
        try {
            this.fromDateTime = LocalDateTime.parse(from, INPUT_DATETIME);
            this.fromTime = true;
        } catch (DateTimeParseException e1) {
            // Not a valid date-time format, try to parse as 'yyyy-MM-dd'
            try {
                this.fromDate = LocalDate.parse(from, INPUT_DATE);
                this.fromTime = false;
            } catch (DateTimeParseException e2) {
                // Not a valid date format
                throw new LebronException("Enter a valid /from format:\n\nyyyy-MM-dd HHmm\nyyyy-MM-dd");
            }
        }
    }

    private void parseTo(String to) throws LebronException {
        if (to == null || to.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /to. Event must end eventually!!");
        }

        to = to.trim();

        // Parse as 'yyyy-MM-dd HHmm'
        try {
            this.toDateTime = LocalDateTime.parse(to, INPUT_DATETIME);
            this.toTime = true;
        } catch (DateTimeParseException e1) {
            // Not a valid date-time format, try to parse as 'yyyy-MM-dd'
            try {
                this.toDate = LocalDate.parse(to, INPUT_DATE);
                this.toTime = false;
            } catch (DateTimeParseException e2) {
                // Not a valid date format
                throw new LebronException("Enter a valid /to format:\n\nyyyy-MM-dd HHmm\nyyyy-MM-dd");
            }
        }
    }

    public String getFrom() {
        if (fromTime) {
            return fromDateTime.format(OUTPUT_DATETIME);
        } else {
            return fromDate.format(OUTPUT_DATE);
        }
    }

    public String getTo() {
        if (toTime) {
            return toDateTime.format(OUTPUT_DATETIME);
        } else {
            return toDate.format(OUTPUT_DATE);
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + "to: " + this.getTo() + ")";
    }
}
