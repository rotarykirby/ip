import java.util.Scanner;

public class Lebron {

    public final static int SIZE = 100;


    public static void handleList(Task[] taskArray) {
        System.out.println("    ____________________________________________________________\n" +
                "    Here are the tasks in your list:");
        for (int i = 0; i < SIZE; i++) {
            if (taskArray[i] == null) {
                break;
            } else {
                System.out.println(String.format("    %d.", (i + 1)) + taskArray[i].toString());
            }
        }
        System.out.println("    ____________________________________________________________");
    }

    public static void mark(Task t, Task[] taskArray) throws LebronException {
        int toMark = Integer.parseInt(t.getDescription().substring(5)) - 1;
        try {
            taskArray[toMark].markDone();
        } catch (NullPointerException e) {
            throw new LebronException("Error - less tasks total than specified.");
        }
        System.out.println("    ____________________________________________________________\n" +
                "    Nice! I've marked this task as done:\n" +
                "      [X] " + taskArray[toMark].getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void unmark(Task t, Task[] taskArray) throws LebronException {
        int toUnmark = Integer.parseInt(t.getDescription().substring(7)) - 1;
        try {
            taskArray[toUnmark].markDone();
        } catch (NullPointerException e) {
            throw new LebronException("Error - less tasks total than specified.");
        }
        System.out.println("    ____________________________________________________________\n" +
                "    OK, I've marked this task as not done yet:\n" +
                "      [ ] " + taskArray[toUnmark].getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void printTask(Task[] taskArray, int i) {
        System.out.println("    ____________________________________________________________\n" +
                "    Got it. I've added this task:\n" +
                "      " + taskArray[i].toString() +
                String.format("\n    Now you have %d %s in the list.", (i + 1) , i == 0 ? "task" : "tasks") +
                "\n    ____________________________________________________________");
    }

    public static void handleTasks(Task[] taskArray, String command, Task t, int i) {
        if (command.startsWith("deadline ")) {
            int pos = command.indexOf("/by ");
            String description = command.substring(9, pos);
            String by = command.substring(pos + 4);
            taskArray[i] = new Deadline(description, by);
            printTask(taskArray, i);
        } else if (command.startsWith("event ")) {
            int pos = command.indexOf("/from ");
            int pos2 = command.indexOf("/to ");
            String description = command.substring(6, pos);
            String from = command.substring(pos + 6, pos2);
            String to = command.substring(pos2 + 4);
            taskArray[i] = new Event(description, from, to);
            printTask(taskArray, i);
        } else if (command.startsWith("todo ")){
            String description = command.substring(5);
            taskArray[i] = new Todo(description);
            printTask(taskArray, i);
        } else {
            // currently unreachable
            taskArray[i] = t;
            printTask(taskArray, i);
        }
    }

    public static void handleInputs() {
        // check for user input and terminate if user says "bye"
        Scanner sc = new Scanner(System.in);
        Task[] taskArray = new Task[SIZE];
        int i = 0;

        String command = sc.nextLine();
        Task t = new Task(command);

        while (!t.getDescription().equals("bye")) {
            // all exception handling done here
            try {
                String desc = t.getDescription();

                if (desc.equals("list")) {
                    handleList(taskArray);
                } else if (desc.startsWith("mark")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - missing index for \"mark\".");
                    }
                    try {
                        Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        throw new LebronException("Error - index must be a number.");
                    }
                    mark(t, taskArray);
                } else if (desc.startsWith("unmark")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - missing index for \"mark\".");
                    }
                    try {
                        Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        throw new LebronException("Error - index must be a number.");
                    }
                    unmark(t, taskArray);
                } else if (desc.startsWith("todo")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of a todo cannot be empty.");
                    }
                    handleTasks(taskArray, command, t, i);
                    ++i;
                } else if (desc.startsWith("deadline")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of a deadline cannot be empty.");
                    }
                    if (!parts[1].contains("/by")) {
                        throw new LebronException("Error - deadline time is missing. Use: deadline <desc> /by <time>");
                    }
                    handleTasks(taskArray, command, t, i);
                    ++i;
                } else if (desc.startsWith("event")) {
                    String[] parts = desc.split("\\s+", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LebronException("Error - description of a event cannot be empty.");
                    }
                    String rest = parts[1];
                    if (!rest.contains("/from") || !rest.contains("/to")) {
                        throw new LebronException("Error - start/end time missing. Use: event <desc> /from <start> /to <end>");
                    }
                    handleTasks(taskArray, command, t, i);
                    ++i;
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
        String greet = "    ____________________________________________________________\n" +
                "    Hello! I'm Lebron\n" +
                "    What can I do for you?\n" +
                "    ____________________________________________________________\n";

        String bye = "    Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________";

        // initial greeting
        System.out.println(greet);

        handleInputs();

        // goodbye message
        System.out.println("    ____________________________________________________________\n" + bye);
    }
}
