import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Lebron {
    private static final String SAVE_FILE_PATH = "./data/Lebron.txt";

    private enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE;

        static CommandType parse(String raw) throws LebronException {
            if (raw == null || raw.isBlank()) {
                throw new LebronException("Please enter a command.");
            }
            String head = raw.trim().split("\\s+", 2)[0];
            return switch (head) {
                case "list" -> LIST;
                case "mark" -> MARK;
                case "unmark" -> UNMARK;
                case "delete" -> DELETE;
                case "todo" -> TODO;
                case "deadline" -> DEADLINE;
                case "event" -> EVENT;
                case "bye" -> BYE;
                default -> throw new LebronException("Error - Unknown Command");
            };
        }
    }

    /**
     * Saves all the tasks into location specified by SAVE_FILE_PATH
     *
     * @param taskList List of tasks
     */
    public static void saveTasks(List<Task> taskList) {
        Path filePath = Paths.get(SAVE_FILE_PATH);

        try {
            Files.createDirectories(filePath.getParent());
            try (FileWriter fw = new FileWriter(SAVE_FILE_PATH)) {
                for (Task task : taskList) {
                    fw.write(formatToWrite(task));
                    fw.write("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not save tasks to file: " + e.getMessage());
        }
    }

    /**
     * Changes the task into a String that can be written into a file.
     *
     * @param t Task specified.
     * @return String to be written into a file.
     */
    private static String formatToWrite(Task t) {
        String taskType;
        String extra1 = "";
        boolean isDone = t.getIsDone();

        if (t instanceof Todo) {
            taskType = "T";
            return taskType + " | " + isDone + " | " + t.getDescription();
        } else if (t instanceof Deadline) {
            taskType = "D";
            extra1 = ((Deadline) t).getOriginalBy();
            return taskType + " | " + isDone + " | " + t.getDescription() + " | " + extra1;
        } else {
            taskType = "E";
            extra1 = ((Event) t).getOriginalFrom();
            String extra2 = ((Event) t).getOriginalTo();
            return taskType + " | " + isDone + " | " + t.getDescription() + " | " + extra1 + " – " + extra2;
        }
    }

    /**
     * Loads the tasks from the save file.
     * Only adds tasks to the returned list if parseTask does not return null
     *
     * @return List of tasks as obtained from the save file.
     */
    public static List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        Path filePath = Paths.get(SAVE_FILE_PATH);

        // if file does not exist, return empty list
        if (!Files.exists(filePath)) {
            return taskList;
        }

        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task t = parseTask(line);
                if (t != null) {
                    taskList.add(t);
                }
            }
        } catch (IOException | LebronException e) {
            System.out.println("Could not load tasks from file:\n" + e.getMessage());
        }

        return taskList;
    }

    /**
     * Takes a line from the save file and re-creates a task based on it
     * Will return null if the task is deemed to have invalid format
     *
     * @param s Line from save file
     * @return Task that is re-created
     */
    private static Task parseTask(String s) throws LebronException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        String[] parts = s.split(" \\| ");
        // check for invalid format
        if (parts.length < 3) {
            return null;
        }

        Task t = null;
        String taskType = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        t = switch (taskType) {
            case "T" -> new Todo(description);
            case "D" -> {
                String by = parts[3].trim();
                yield new Deadline(description, by);
            }
            case "E" -> {
                String[] fromTo = parts[3].trim().split("–", 2);
                yield new Event(description, fromTo[0], fromTo[1]);
            }
            default -> t;
        };

        if (t != null && isDone) {
            t.markDone();
        }

        return t;
    }

    public static void handleList(List<Task> taskList) {
        System.out.println("    ____________________________________________________________\n" +
                "    Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i) == null) {
                break;
            } else {
                System.out.println(String.format("    %d.", (i + 1)) + taskList.get(i).toString());
            }
        }
        System.out.println("    ____________________________________________________________");
    }

    public static void mark(Task t, List<Task> taskList) throws LebronException {
        int toMark = Integer.parseInt(t.getDescription().substring(5)) - 1;
        try {
            taskList.get(toMark).markDone();
        } catch (IndexOutOfBoundsException e) {
            throw new LebronException("Error - less tasks total than specified.");
        }
        System.out.println("    ____________________________________________________________\n" +
                "    Nice! I've marked this task as done:\n" +
                "      [X] " + taskList.get(toMark).getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void unmark(Task t, List<Task> taskList) throws LebronException {
        int toUnmark = Integer.parseInt(t.getDescription().substring(7)) - 1;
        try {
            taskList.get(toUnmark).markUndone();
        } catch (IndexOutOfBoundsException e) {
            throw new LebronException("Error - less tasks total than specified.");
        }
        System.out.println("    ____________________________________________________________\n" +
                "    OK, I've marked this task as not done yet:\n" +
                "      [ ] " + taskList.get(toUnmark).getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void printTask(List<Task> taskList, int i) {
        System.out.println("    ____________________________________________________________\n" +
                "    Got it. I've added this task:\n" +
                "      " + taskList.get(i).toString() +
                String.format("\n    Now you have %d %s in the list.", (i + 1) , i == 0 ? "task" : "tasks") +
                "\n    ____________________________________________________________");
    }

    public static void handleTasks(List<Task> taskList, String command, Task t, int i) throws LebronException {
        if (command.startsWith("deadline ")) {
            int pos = command.indexOf("/by ");
            String description = command.substring(9, pos).trim();
            String by = command.substring(pos + 4).trim();
            taskList.add(new Deadline(description, by));
            printTask(taskList, i);
        } else if (command.startsWith("event ")) {
            int pos = command.indexOf("/from ");
            int pos2 = command.indexOf("/to ");
            String description = command.substring(6, pos).trim();
            String from = command.substring(pos + 6, pos2).trim();
            String to = command.substring(pos2 + 4).trim();
            if (from.contains("–") || to.contains("–")) {
                throw new LebronException("Failed to add event. " +
                        "Please try again without using \"–\" (en dash) as a character.");
            }
            taskList.add(new Event(description, from, to));
            printTask(taskList, i);
        } else if (command.startsWith("todo ")){
            String description = command.substring(5);
            taskList.add(new Todo(description));
            printTask(taskList, i);
        } else {
            // currently unreachable
            taskList.add(t);
            printTask(taskList, i);
        }
    }

    public static void deleteTask(Task t, List<Task> taskList, int i) throws LebronException {
        int toDelete = Integer.parseInt(t.getDescription().substring(7)) - 1;
        try {
            Task removed = taskList.remove(toDelete);
            System.out.println("    ____________________________________________________________\n" +
                    "    Noted. I've removed this task:\n" +
                    "      " + removed.toString() +
                    String.format("\n    Now you have %d %s in the list.", --i , i == 1 ? "task" : "tasks") +
                    "\n    ____________________________________________________________");
        } catch (IndexOutOfBoundsException e) {
            throw new LebronException("Error - less tasks total than specified.");
        }
    }

    public static void handleInputs() {
        // check for user input and terminate if user says "bye"
        Scanner sc = new Scanner(System.in);
        List<Task> taskList = loadTasks();
        int i = taskList.size();

        String command = sc.nextLine();
        Task t = new Task(command);

        while (!t.getDescription().equals("bye")) {
            // exception handling done here
            try {
                String desc = t.getDescription();
                CommandType type = CommandType.parse(desc);
                switch (type) {
                    case BYE:
                        return;
                    case LIST: {
                        handleList(loadTasks());
                        break;
                    }
                    case MARK: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - missing index for \"mark\".");
                        }
                        try {
                            Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            throw new LebronException("Error - task to be marked must be a number.");
                        }
                        mark(t, taskList);
                        saveTasks(taskList);
                        break;
                    }
                    case UNMARK: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - missing index for \"mark\".");
                        }
                        try {
                            Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            throw new LebronException("Error - task to be unmarked must be a number.");
                        }
                        unmark(t, taskList);
                        saveTasks(taskList);
                        break;
                    }
                    case DELETE: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - task to delete not specified.");
                        }
                        try {
                            Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            throw new LebronException("Error - task to be deleted must be a number.");
                        }
                        deleteTask(t, taskList, i);
                        --i;
                        saveTasks(taskList);
                        break;
                    }
                    case TODO: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - description of a todo cannot be empty.");
                        }
                        handleTasks(taskList, command, t, i);
                        ++i;
                        saveTasks(taskList);
                        break;
                    }
                    case DEADLINE: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - description of a deadline cannot be empty.");
                        }
                        if (!parts[1].contains("/by")) {
                            throw new LebronException("Error - deadline description missing time.\n" +
                                    "    Use: deadline <desc> /by <time>");
                        }
                        handleTasks(taskList, command, t, i);
                        ++i;
                        saveTasks(taskList);
                        break;
                    }
                    case EVENT: {
                        String[] parts = desc.split("\\s+", 2);
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new LebronException("Error - description of an event cannot be empty.");
                        }
                        String rest = parts[1];
                        if (!rest.contains("/from") || !rest.contains("/to")) {
                            throw new LebronException("Error - event description missing start/end time.\n" +
                                    "    Use: event <desc> /from <start> /to <end>");
                        }
                        handleTasks(taskList, command, t, i);
                        ++i;
                        saveTasks(taskList);
                        break;
                    }

                }
            } catch (LebronException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            }

            command = sc.nextLine();
            t = new Task(command);
        }
    }

    public static void main(String[] args) {
        String greet = """
                    ____________________________________________________________
                    Hello! I'm Lebron
                    What can I do for you?
                    ____________________________________________________________
                """;

        String bye = "    Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________";

        // initial greeting
        System.out.println(greet);

        handleInputs();

        // goodbye message
        System.out.println("    ____________________________________________________________\n" + bye);
    }
}
