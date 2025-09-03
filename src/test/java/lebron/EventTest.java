package lebron;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    void isOnDateDatetimeSpanningMidnightIncludesBothDays() throws LebronException {
        Event e = new Event("Overnight", "2025-08-26 2330", "2025-08-27 0030");

        assertTrue(e.isOnDate(LocalDate.of(2025, 8, 26)));
        assertTrue(e.isOnDate(LocalDate.of(2025, 8, 27)));
        assertFalse(e.isOnDate(LocalDate.of(2025, 8, 25)));
        assertFalse(e.isOnDate(LocalDate.of(2025, 8, 28)));
    }

    @Test
    void isOnDateDateOnly() throws LebronException {
        Event e = new Event("Camp", "2025-09-10", "2025-09-12");

        // Inclusive between start and end
        assertTrue(e.isOnDate(LocalDate.of(2025, 9, 10)));
        assertTrue(e.isOnDate(LocalDate.of(2025, 9, 11)));
        assertTrue(e.isOnDate(LocalDate.of(2025, 9, 12)));

        // Outside range
        assertFalse(e.isOnDate(LocalDate.of(2025, 9, 9)));
        assertFalse(e.isOnDate(LocalDate.of(2025, 9, 13)));
    }

    @Test
    void datetimeStartAfterEndThrows() {
        assertThrows(LebronException.class, () ->
                new Event("Bad times", "2025-8-27 1609", "2025-8-27 1608")
        );
    }

    @Test
    void invalidFormatsThrows() {
        // Slashes not allowed
        assertThrows(LebronException.class, () ->
                new Event("Bad format", "2025/08/27 0900", "2025/08/27 1000")
        );

        assertThrows(LebronException.class, () ->
                new Event("Bad date", "2025-13-01", "2025-13-02")
        );
    }

    @Test
    void toStringContainsKeyParts() throws LebronException {
        Event e = new Event("LEBRONNNN", "2025-08-27 0900", "2025-08-27 1000");
        String s = e.toString();
        assertTrue(s.startsWith("[E]"));
        assertTrue(s.contains("LEBRONNNN"));
        assertTrue(s.contains("(from:"));
        assertTrue(s.contains("to:"));
        assertTrue(s.endsWith(")"));
    }
}

