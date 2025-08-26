package lebron;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected LocalDate fromDate;
    protected LocalDateTime fromDateTime;
    protected LocalDate toDate;
    protected LocalDateTime toDateTime;
    protected boolean time;

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-M-d HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Event(String description, String from, String to) throws LebronException {
        super(description);
        parseFromTo(from, to);
    }

    private void parseFromTo(String from, String to) throws LebronException {
        if (from == null || from.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /from. Consider using deadline if no start time.");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new LebronException("Cannot have empty /to. Lebron.Event must end eventually!!");
        }

        from = from.trim();
        to = to.trim();

        // Parse as 'yyyy-MM-dd HHmm'
        try {
            this.fromDateTime = LocalDateTime.parse(from, INPUT_DATETIME);
            this.toDateTime = LocalDateTime.parse(to, INPUT_DATETIME);
            this.time = true;
            if (fromDateTime.isAfter(toDateTime)) {
                throw new LebronException("Lebron.Event start time cannot be after end time");
            }
            return;
        } catch (DateTimeParseException e1) {
            // Not a valid date-time format, try to parse as 'yyyy-MM-dd'
            try {
                this.fromDate = LocalDate.parse(from, INPUT_DATE);
                this.toDate = LocalDate.parse(to, INPUT_DATE);
                this.time = false;
                if (fromDate.isAfter(toDate)) {
                    throw new LebronException("Lebron.Event start date cannot be after end date");
                }
                return;
            } catch (DateTimeParseException e2) {
                // Not a valid date format
                System.out.println(from);
                throw new LebronException("Enter dates in a valid format:\n\n" +
                        "    yyyy-MM-dd HHmm\n" +
                        "    yyyy-MM-dd\n\n" +
                        "    Note that both Start and End must have the same time format.");
            }
        }
    }

    public String getFrom() {
        if (time) {
            return fromDateTime.format(OUTPUT_DATETIME);
        } else {
            return fromDate.format(OUTPUT_DATE);
        }
    }

    public String getTo() {
        if (time) {
            return toDateTime.format(OUTPUT_DATETIME);
        } else {
            return toDate.format(OUTPUT_DATE);
        }
    }

    public String getOriginalFrom() {
        if (time) {
            return fromDateTime.format(INPUT_DATETIME);
        } else {
            return fromDate.format(INPUT_DATE);
        }
    }

    public String getOriginalTo() {
        if (time) {
            return toDateTime.format(INPUT_DATETIME);
        } else {
            return toDate.format(INPUT_DATE);
        }
    }

    public boolean isOnDate(LocalDate date) {
        LocalDate from = time ? fromDateTime.toLocalDate() : fromDate;
        LocalDate to = time ? toDateTime.toLocalDate() : toDate;
        return !date.isBefore(from) && !date.isAfter(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}
