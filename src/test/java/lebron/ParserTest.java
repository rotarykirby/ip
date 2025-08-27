package lebron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void parse_event_with_no_time_throws() {
        assertThrows(LebronException.class, () ->
                Parser.parse("event test /from 1234-2-2 1609 /to      ")
        );
    }

    @Test
    public void parse_unmark_out_of_range() {
        assertThrows(LebronException.class, () -> Parser.parse("unmark -1"));
    }


    @Test
    void parse_todo_trimsAndParses_desc() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("  todo   read book  ");
        assertEquals(Parser.CommandType.TODO, pc.type);
        assertEquals("read book", pc.arg1);
    }

    @Test
    void parse_deadline_extracts_desc_and_by() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("deadline submit report /by 2025-08-27");
        assertEquals(Parser.CommandType.DEADLINE, pc.type);
        assertEquals("submit report", pc.arg1);
        assertEquals("2025-08-27", pc.arg2);
    }

    @Test
    void parse_event_extracts_desc_from_to() throws LebronException {
        Parser.ParsedCommand pc = Parser.parse("event project mtg /from 2025-08-26 /to 2025-08-27");
        assertEquals(Parser.CommandType.EVENT, pc.type);
        assertEquals("project mtg", pc.arg1);
        assertEquals("2025-08-26", pc.arg2);
        assertEquals("2025-08-27", pc.arg3);
    }

    @Test
    void parse_list_with_args_throws() {
        LebronException ex = assertThrows(LebronException.class,
                () -> Parser.parse("list extra"));
        assertTrue(ex.getMessage().toLowerCase().contains("list"));
    }

    @Test
    void parse_delete_zero_or_negative_index_throws() {
        assertThrows(LebronException.class, () -> Parser.parse("delete 0"));
        assertThrows(LebronException.class, () -> Parser.parse("delete -3"));
    }

    @Test
    void parse_unknown_or_empty_throws() {
        assertThrows(LebronException.class, () -> Parser.parse("abracadabra"));
        assertThrows(LebronException.class, () -> Parser.parse(""));
    }
}
