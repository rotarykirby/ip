package lebron;

/**
 * Parses raw user input strings into structured commands that the program can then interpret and execute.
 */
public class Parser {
    /**
     * Represents the different types of commands that can be issued by the user.
     * Each command corresponds to a specific action that the application can perform.
     */
    public enum CommandType { HI, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE, CHECK, FIND, UNDO }

    /**
     * Class that ensures all the commands are formatted properly
     * and its individual parts can be accessed easily
     */
    public static final class ParsedCommand {
        private CommandType type;
        private String arg1;
        private String arg2;
        private String arg3;
        private int index;

        private ParsedCommand(CommandType type, String arg1, String arg2, String arg3, int index) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.index = index;
        }

        public CommandType getType() {
            return this.type;
        }

        public String getArg1() {
            return this.arg1;
        }

        public String getArg2() {
            return this.arg2;
        }

        public String getArg3() {
            return this.arg3;
        }

        public int getIndex() {
            return this.index;
        }
    }

    /**
     * Parses raw user input into a structured command.
     *
     * @param raw the raw user input.
     * @return the parsedCommand.
     * @throws LebronException if the user input does not follow the format of the command supported.
     */
    public static ParsedCommand parse(String raw) throws LebronException {
        if (raw == null || raw.isEmpty()) {
            throw new LebronException("Error - command cannot be empty.");
        }
        String[] first = raw.trim().split("\\s+", 2);
        String head = first[0].toLowerCase().trim();
        String rest = first.length > 1 ? first[1] : "";

        switch (head) {
        case "hi":
            return new ParsedCommand(CommandType.HI, null, null, null, -1);
        case "bye":
            return new ParsedCommand(CommandType.BYE, null, null, null, -1);
        case "list":
            if (!rest.isEmpty()) {
                throw new LebronException("Error - command 'list' does not take arguments.");
            }
            return new ParsedCommand(CommandType.LIST, null, null, null, -1);
        case "mark":
            return new ParsedCommand(CommandType.MARK, null, null, null,
                    parseIndex(rest, "mark <index>"));
        case "unmark":
            return new ParsedCommand(CommandType.UNMARK, null, null, null,
                    parseIndex(rest, "unmark <index>"));
        case "delete":
            return new ParsedCommand(CommandType.DELETE, null, null, null,
                    parseIndex(rest, "delete <index>"));
        case "todo":
            if (rest.isEmpty()) {
                throw new LebronException("Error - description of a todo cannot be empty.");
            }
            return new ParsedCommand(CommandType.TODO, rest, null, null, -1);
        case "deadline": {
            int byPos = rest.indexOf("/by");
            if (byPos < 0) {
                throw new LebronException("Error - Use: deadline <description> /by <date>");
            }
            String desc = rest.substring(0, byPos).trim();
            String by = rest.substring(byPos + 3).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new LebronException("Error - deadline needs description and /by <date>.");
            }
            return new ParsedCommand(CommandType.DEADLINE, desc, by, null, -1);
        }
        case "event": {
            int fromPart = rest.indexOf("/from");
            int toPart = rest.indexOf("/to");
            if (fromPart < 0 || toPart < 0 || toPart <= fromPart) {
                throw new LebronException("Error - Use: event <description> /from <from> /to <to>");
            }
            String desc = rest.substring(0, fromPart).trim();
            String from = rest.substring(fromPart + 5, toPart).trim();
            String to = rest.substring(toPart + 3).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new LebronException("Error - event needs description, /from and /to.");
            } else if (from.contains("–") || to.contains("–")) {
                throw new LebronException("Error - event start and end hasTime cannot contain character \"–\"");
            }
            return new ParsedCommand(CommandType.EVENT, desc, from, to, -1);
        }
        case "check":
            if (rest.isEmpty()) {
                throw new LebronException("Error - Use: check <yyyy-MM-dd>");
            }
            return new ParsedCommand(CommandType.CHECK, rest, null, null, -1);
        case "find":
            if (rest.isEmpty()) {
                throw new LebronException("Error - keyword(s) not specified.");
            }
            return new ParsedCommand(CommandType.FIND, rest, null, null, -1);
        case "undo": {
            return new ParsedCommand(CommandType.UNDO, null, null, null, -1);
        }

        default:
            throw new LebronException("Error - Lebron does not know what you are talking about.");
        }
    }

    /**
     * Determines the index of the task.
     *
     * @param s the index of task to be marked/unmarked/deleted.
     * @param usage the correct, formatted usage instruction.
     * @return the int of the index.
     * @throws LebronException if there was a missing index or if the index specified was not a positive integer.
     */
    private static int parseIndex(String s, String usage) throws LebronException {
        s = s.trim();
        if (s.isEmpty()) {
            throw new LebronException("Error - missing index. Use: " + usage);
        }
        try {
            int val = Integer.parseInt(s);
            if (val <= 0) {
                throw new NumberFormatException();
            }
            return val;
        } catch (NumberFormatException e) {
            throw new LebronException("Error - index must be a positive integer.");
        }
    }

}
