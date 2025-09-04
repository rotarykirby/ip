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
    private final Scanner sc = new Scanner(System.in);

    /**
     * Prints greeting banner when Lebron starts.
     */
    public String greeting() {
        return "Hello! I'm Lebron\n" + "What can I do for you?";
    }

    /**
     * Prints goodbye banner before Lebron exits.
     */
    public String bye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Shows a loading error message to the user when tasks are unable to be loaded from file location
     *
     * @param message the error message containing information on why loading failed.
     */
    public String showLoadingError(String message) {
        return "Error - Could not load tasks from file:\n" + "message";
    }

    /**
     * Reads one line of input from the user. Returns null if next line is empty.
     *
     * @return the trimmed line (if any).
     */
    public String readCommand() {
        if (!sc.hasNextLine()) {
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
    public String handleFind(TaskList taskList, String keyword) {
        keyword = keyword.trim().toLowerCase();
        List<Task> matches = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            String desc = taskList.get(i).getDescription();
            if (desc.toLowerCase().contains(keyword)) {
                matches.add(taskList.get(i));
            }
        }

        int size = matches.size();

        if (matches.isEmpty()) {
            return "No matching tasks found.";
        } else if (size == 1) {
            return "Here is the only matching task in your list:\n" + matches.get(0);
        } else {
            StringBuilder tasks = new StringBuilder();
            for (Task match : matches) {
                tasks.append("\n").append(match);
            }
            return "Here are the matching tasks in your list" + tasks;
        }
    }

    /**
     * Prints all tasks that occur on the specified date.
     *
     * @param taskList the list of tasks currently added.
     * @param date the date.
     * @throws LebronException if there is an invalid date format.
     */
    public String handleCheck(TaskList taskList, String date) throws LebronException {
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

        if (tasksOnDate.isEmpty()) {
            return("No tasks scheduled for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasksOnDate.size(); i++) {
                sb.append("\n").append(i + 1).append(". ").append(tasksOnDate.get(i).toString());
            }
            return"Tasks scheduled for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":" + sb;
        }
    }

    /**
     * Prints the entire list of tasks.
     *
     * @param taskList the list of tasks currently added.
     */
    public String handleList(TaskList taskList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i) == null) {
                break;
            } else {
                sb.append("\n").append(i + 1).append(". ").append(taskList.get(i).toString());
            }
        }

        return"Here are the tasks in your list:" + sb;
    }

    /**
     * Shows a confirmation that a task was marked as done.
     *
     * @param t the task.
     */
    public String showMarked(Task t) {
        return"Nice! I've marked this task as done:\n    [X] " + t.getDescription();
    }

    /**
     * Shows a confirmation that a task was marked as not done.
     *
     * @param t the task.
     */
    public String showUnmarked(Task t) {
        return"OK, I've marked this task as not done yet:\n    [ ] " + t.getDescription();
    }

    /**
     * Shows a confirmation that a task was deleted from the list.
     * Shows the remaining tasks left.
     *
     * @param removed the removed task.
     * @param size the new number of tasks in the new list.
     */
    public String showDeleted(Task removed, int size) {
        return"Noted. I've removed this task:\n" + "      " + removed.toString()
                + String.format("\nNow you have %d %s in the list.", size , size == 1 ? "task" : "tasks");
    }

    /**
     * Shows a confirmation that a task was added to the list.
     * Shows the total tasks count.
     *
     * @param t the added task.
     * @param size the new number of tasks in the new list.
     */
    public String showAdded(Task t, int size) {
        return"Got it. I've added this task:\n" + "      " + t.toString()
                + String.format("\nNow you have %d %s in the list.", size , size == 0 ? "task" : "tasks");
    }

    /**
     * Prints an error message.
     *
     * @param msg the error message.
     */
    public String showError(String msg) {
        return msg;
    }
}
