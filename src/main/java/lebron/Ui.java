package lebron;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that is responsible for all user interaction - reading commands and writing formatted output.
 */
public class Ui {
    private static final String LINE = "    ____________________________________________________________";
    private final Scanner sc = new Scanner(System.in);

    /**
     * Prints greeting banner when Lebron starts.
     */
    public void greeting() {
        System.out.println(LINE + "\n    Hello! I'm Lebron\n" + "    What can I do for you?\n" + LINE);
    }

    /**
     * Prints goodbye banner before Lebron exits.
     */
    public void bye() {
        System.out.println(LINE + "\n    Bye. Hope to see you again soon!\n" + LINE);
    }

    /**
     * Shows a loading error message to the user when tasks are unable to be loaded from file location
     *
     * @param message the error message containing information on why loading failed.
     */
    public void showLoadingError(String message) {
        System.out.println(LINE);
        System.out.println("    Error - Could not load tasks from file:");
        System.out.println("    " + message);
        System.out.println(LINE);
    }

    /**
     * Reads one line of input from the user. Returns null if next line is empty.
     *
     * @return the trimmed line (if any).
     */
    public String readCommand() {
        if (!sc.hasNextLine()){
            return null;
        }
        return sc.nextLine().trim();
    }

    /**
     * Identifies matches in substring between tasks in tasklist and the keyword.
     * Prints all the matches, if any
     *
     * @param taskList list of tasks
     * @param keyword substring to search for
     */
    public void handleFind(TaskList taskList, String keyword) {
        keyword = keyword.trim().toLowerCase();
        List<Task> matches = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            String desc = taskList.get(i).getDescription();
            if (desc.toLowerCase().contains(keyword)) {
                matches.add(taskList.get(i));
            }
        }

        int size = matches.size();

        System.out.println(LINE);
        if (matches.isEmpty()) {
            System.out.println("    No matching tasks found.");
        } else if (size == 1) {
            System.out.println("    Here is the only matching task in your list:");
            System.out.println("    " + matches.get(0));
        } else {
            System.out.println("    Here are the matching tasks in your list");
            for (int i = 0; i < size; i++) {
                System.out.println("    " + matches.get(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Prints all tasks that occur on the specified date.
     *
     * @param taskList the list of tasks currently added.
     * @param date the date.
     * @throws LebronException if there is an invalid date format.
     */
    public void handleCheck(TaskList taskList, String date) throws LebronException {
        LocalDate targetDate;

        try {
            targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException e) {
            throw new LebronException("Error - invalid date format. Use yyyy-MM-dd format.");
        }

        List<Task> tasksOnDate = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task instanceof Deadline) {
                if (((Deadline) task).isOnDate(targetDate)) {
                    tasksOnDate.add(task);
                }
            } else if (task instanceof Event) {
                if (((Event) task).isOnDate(targetDate)) {
                    tasksOnDate.add(task);
                }
            }
        }

        System.out.println(LINE);
        if (tasksOnDate.isEmpty()) {
            System.out.println("    No tasks scheduled for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        } else {
            System.out.println("    Tasks scheduled for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            for (int i = 0; i < tasksOnDate.size(); i++) {
                System.out.println("    " + (i + 1) + "." + tasksOnDate.get(i).toString());
            }
        }
        System.out.println(LINE);
    }

    /**
     * Prints the entire list of tasks.
     *
     * @param taskList the list of tasks currently added.
     */
    public void handleList(TaskList taskList) {
        System.out.println(LINE + "\n    Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i) == null) {
                break;
            } else {
                System.out.println(String.format("    %d.", (i + 1)) + taskList.get(i).toString());
            }
        }
        System.out.println(LINE);
    }

    /**
     * Shows a confirmation that a task was marked as done.
     *
     * @param t the task.
     */
    public void showMarked(Task t) {
        System.out.println(LINE + "\n    Nice! I've marked this task as done:\n" +
                "      [X] " + t.getDescription() +
                "\n" + LINE);
    }

    /**
     * Shows a confirmation that a task was marked as not done.
     *
     * @param t the task.
     */
    public void showUnmarked(Task t) {
        System.out.println(LINE + "\n    OK, I've marked this task as not done yet:\n" +
                "      [ ] " + t.getDescription() +
                "\n" + LINE);
    }

    /**
     * Shows a confirmation that a task was deleted from the list.
     * Shows the remaining tasks left.
     *
     * @param removed the removed task.
     * @param size the new number of tasks in the new list.
     */
    public void showDeleted(Task removed, int size) {
        System.out.println(LINE + "\n    Noted. I've removed this task:\n" +
                "      " + removed.toString() +
                String.format("\n    Now you have %d %s in the list.", size , size == 1 ? "task" : "tasks") +
                "\n" + LINE);
    }

    /**
     * Shows a confirmation that a task was added to the list.
     * Shows the total tasks count.
     *
     * @param t the added task.
     * @param size the new number of tasks in the new list.
     */
    public void showAdded(Task t, int size) {
        System.out.println(LINE + "\n    Got it. I've added this task:\n" +
                "      " + t.toString() +
                String.format("\n    Now you have %d %s in the list.", size , size == 0 ? "task" : "tasks") +
                "\n" + LINE);
    }

    /**
     * Prints an error message.
     *
     * @param msg the error message.
     */
    public void showError(String msg) {
        System.out.println(LINE + "\n    " + msg + "\n" + LINE);
    }
}
