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
                System.out.println(String.format("     %d. [%s] ", (i + 1), taskArray[i].getStatusIcon()) +
                        taskArray[i].getDescription());
            }
        }
        System.out.println("    ____________________________________________________________");
    }

    public static void mark(Task t, Task[] taskArray) {
        int toMark = Integer.parseInt(t.getDescription().substring(5)) - 1;
        taskArray[toMark].markDone();
        System.out.println("    ____________________________________________________________\n" +
                "    Nice! I've marked this task as done:\n" +
                "      [X] " + taskArray[toMark].getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void unmark(Task t, Task[] taskArray) {
        int toUnmark = Integer.parseInt(t.getDescription().substring(7)) - 1;
        taskArray[toUnmark].unmarkDone();
        System.out.println("    ____________________________________________________________\n" +
                "    OK, I've marked this task as not done yet:\n" +
                "      [ ] " + taskArray[toUnmark].getDescription() +
                "\n    ____________________________________________________________");
    }

    public static void handleInputs() {
        // check for user input and terminate if user says "bye"
        Scanner sc = new Scanner(System.in);
        Task[] taskArray = new Task[SIZE];
        int i = 0;

        Task t = new Task(sc.nextLine());
        while (!t.getDescription().equals("bye")) {
            if (t.getDescription().equals("list")) {
                handleList(taskArray);
            } else if (t.getDescription().startsWith("mark ")) {
                mark(t, taskArray);
            } else if (t.getDescription().startsWith("unmark")) {
                unmark(t, taskArray);
            } else {
                taskArray[i] = t;
                ++i;
                System.out.println("    ____________________________________________________________\n" +
                        "    added: " + t.getDescription() +
                        "\n    ____________________________________________________________");
            }
            t = new Task(sc.nextLine());
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
