import java.util.Scanner;

public class Lebron {
    public final static int SIZE = 100;

    public static void handleList(String[] listArray) {
        System.out.println("    ____________________________________________________________");
        for (int i = 0; i < SIZE; i++) {
            if (listArray[i] == null) {
                break;
            } else {
                System.out.println(String.format("     %d. ", (i + 1)) + listArray[i]);
            }
        }
        System.out.println("    ____________________________________________________________");
    }

    public static void handleInputs() {
        // check for user input and terminate if user says "bye"
        Scanner sc = new Scanner(System.in);
        String[] listArray = new String[SIZE];
        int i = 0;

        String input = sc.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                handleList(listArray);
            } else {
                listArray[i] = input;
                ++i;
                System.out.println("    ____________________________________________________________\n" +
                        "    added: " + input +
                        "\n    ____________________________________________________________");
            }
            input = sc.nextLine();
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
