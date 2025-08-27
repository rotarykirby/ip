package lebron;

public class Parser {
    public enum CommandType { LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE, CHECK; }

    public static final class ParsedCommand {
        public final CommandType type;
        public final String arg1, arg2, arg3;
        public final int index;

        private ParsedCommand(CommandType type, String arg1, String arg2, String arg3, int index) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.index = index;
        }
    }

    public static ParsedCommand parse(String raw) throws LebronException {
        if (raw == null || raw.isEmpty()) {
            throw new LebronException("Error - command cannot be empty.");
        }
        String[] first = raw.trim().split("\\s+", 2);
        String head = first[0].toLowerCase().trim();
        String rest = first.length > 1 ? first[1] : "";

        switch (head) {
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
                throw new LebronException("Error - event start and end time cannot contain character \"–\"");
            }
            return new ParsedCommand(CommandType.EVENT, desc, from, to, -1);
        }

        case "check":
            if (rest.isEmpty()) {
                throw new LebronException("Error - Use: check <yyyy-MM-dd>");
            }
            return new ParsedCommand(CommandType.CHECK, rest, null, null, -1);

        default:
            throw new LebronException("Error - What talking you?");
        }
    }

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
