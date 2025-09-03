package lebron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void parseEventWithNoTimeThrows() {
        assertThrows(LebronException.class, () ->
                Parser.parse("event test /from 1234-2-2 1609 /to      ")
        );
    }

    @Test
    public void parseUnmarkOutOfRange() {
        assertThrows(LebronException.class, () -> Parser.parse("unmark -1"));
    }


    @Test
    void parseToDoTrimsAndParsesDesc() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("  todo   read book  ");
        assertEquals(Parser.CommandType.TODO, pc.getType());
        assertEquals("read book", pc.getArg1());
    }

    @Test
    void parseDeadlineExtractsDescAndBy() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("deadline submit report /by 2025-08-27");
        assertEquals(Parser.CommandType.DEADLINE, pc.getType());
        assertEquals("submit report", pc.getArg1());
        assertEquals("2025-08-27", pc.getArg2());
    }

    @Test
    void parseEventExtractsDescFromTo() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("event project mtg /from 2025-08-26 /to 2025-08-27");
        assertEquals(Parser.CommandType.EVENT, pc.getType());
        assertEquals("project mtg", pc.getArg1());
        assertEquals("2025-08-26", pc.getArg2());
        assertEquals("2025-08-27", pc.getArg3());
    }

    @Test
    void parseListWithArgsThrows() {
        LebronException ex = assertThrows(LebronException.class, () ->
                Parser.parse("list extra"));
        assertTrue(ex.getMessage().toLowerCase().contains("list"));
    }

    @Test
    void parseDeleteZeroOrNegativeIndexThrows() {
        assertThrows(LebronException.class, () -> Parser.parse("delete 0"));
        assertThrows(LebronException.class, () -> Parser.parse("delete -3"));
    }

    @Test
    void parseUnknownOrEmptyThrows() {
        assertThrows(LebronException.class, () -> Parser.parse("abracadabra"));
        assertThrows(LebronException.class, () -> Parser.parse(""));
    }
}
