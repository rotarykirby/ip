import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "    ____________________________________________________________";
    private final Scanner sc = new Scanner(System.in);

    public void greeting() {
        System.out.println(LINE + "\n    Hello! I'm Lebron\n" + "    What can I do for you?\n" + LINE);
    }

    public void bye() {
        System.out.println(LINE + "\n    Bye. Hope to see you again soon!\n" + LINE);
    }

    public void showLoadingError(String message) {
        System.out.println(LINE);
        System.out.println("    Error - Could not load tasks from file:");
        System.out.println("    " + message);
        System.out.println(LINE);
    }

    public String readCommand() {
        if (!sc.hasNextLine()){
            return null;
        }
        return sc.nextLine().trim();
    }

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

    public void showMarked(Task t) {
        System.out.println(LINE + "\n    Nice! I've marked this task as done:\n" +
                "      [X] " + t.getDescription() +
                "\n" + LINE);
    }

    public void showUnmarked(Task t) {
        System.out.println(LINE + "\n    OK, I've marked this task as not done yet:\n" +
                "      [ ] " + t.getDescription() +
                "\n" + LINE);
    }

    public void showDeleted(Task removed, int size) {
        System.out.println(LINE + "\n    Noted. I've removed this task:\n" +
                "      " + removed.toString() +
                String.format("\n    Now you have %d %s in the list.", size , size == 1 ? "task" : "tasks") +
                "\n" + LINE);
    }

    public void showAdded(Task t, int size) {
        System.out.println(LINE + "\n    Got it. I've added this task:\n" +
                "      " + t.toString() +
                String.format("\n    Now you have %d %s in the list.", size , size == 0 ? "task" : "tasks") +
                "\n" + LINE);
    }

    public void showError(String msg) {
        System.out.println(LINE + "\n    " + msg + "\n" + LINE);
    }
}
