import java.util.Scanner;

public class Lebron {
    public static void main(String[] args) {
        String greet = "    ____________________________________________________________\n" +
                "    Hello! I'm Lebron\n" +
                "    What can I do for you?\n" +
                "    ____________________________________________________________\n";

        String bye = "    Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________";

        // initial greeting
        System.out.println(greet);

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equals("bye")) {
            System.out.println("    ____________________________________________________________\n    " +
                    input +
                    "\n    ____________________________________________________________");
            input = sc.nextLine();
        }

        System.out.println("    ____________________________________________________________\n" + bye);
    }
}
