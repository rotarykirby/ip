import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Lebron {
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
            taskList.get(toUnmark).markDone();
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

    public static void handleTasks(List<Task> taskList, String command, Task t, int i) {
        if (command.startsWith("deadline ")) {
            int pos = command.indexOf("/by ");
            String description = command.substring(9, pos);
            String by = command.substring(pos + 4);
            taskList.add(new Deadline(description, by));
            printTask(taskList, i);
        } else if (command.startsWith("event ")) {
            int pos = command.indexOf("/from ");
            int pos2 = command.indexOf("/to ");
            String description = command.substring(6, pos);
            String from = command.substring(pos + 6, pos2);
            String to = command.substring(pos2 + 4);
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
        List<Task> taskList = new ArrayList<>();
        int i = 0;

        String command = sc.nextLine();
        Task t = new Task(command);

        while (!t.getDescription().equals("bye")) {
            // all exception handling done here
            try {
                String desc = t.getDescription();

                if (desc.equals("list")) {
                    handleList(taskList);
                } else if (desc.startsWith("mark")) {
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
                } else if (desc.startsWith("unmark")) {
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
                } else if (desc.startsWith("todo")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of a todo cannot be empty.");
                    }
                    handleTasks(taskList, command, t, i);
                    ++i;
                } else if (desc.startsWith("deadline")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of a deadline cannot be empty.");
                    }
                    if (!parts[1].contains("/by")) {
                        throw new LebronException("Error - deadline description missing time. \nUse: deadline <desc> /by <time>");
                    }
                    handleTasks(taskList, command, t, i);
                    ++i;
                } else if (desc.startsWith("event")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of an event cannot be empty.");
                    }
                    String rest = parts[1];
                    if (!rest.contains("/from") || !rest.contains("/to")) {
                        throw new LebronException("Error - event description missing start/end time. \nUse: event <desc> /from <start> /to <end>");
                    }
                    handleTasks(taskList, command, t, i);
                    ++i;
                } else if (desc.startsWith("delete")) {
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
                } else {
                    throw new LebronException("Error - what talking you?");
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
