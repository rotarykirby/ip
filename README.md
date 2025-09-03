# Lebron
> "If my mom had balls she'd be my dad"  – Max Verstappen ([source](https://www.youtube.com/watch?v=Nq82CB-zOto))

We all know who Lebron is. He is not only the cutest pookie **and** the greatest player of all time, he is now also your personalised chat bot that is:
- Free
- Open-source
- Lightning fast


All you need to do the get your own Lebron is,
1. download him from [here](https://github.com/rotarykirby/ip/releases/tag/A-Jar)
2. double-click it.
3. add your tasks.
4. let it manage your tasks for you 😉

And you can then enjoy your very own Lebron!

Features:
- [x] Keep track of your to do tasks, deadlines, and events
- [x] Mark tasks as complete/incomplete
- [x] Check what deadlines/events occur on a specific date


---
Note for Java programmers: you may change the folder where task's data is stored by changing the string in the `main` method of the Lebron class as shown below:
```java
public class Lebron {
    public static void main(String[] args) {
        new Lebron("./data/Lebron.txt").run();
    }
}
```

---
## Setting up in Intellij

Prerequisites: JDK 17, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Lebron.java` file, right-click it, and choose `Run Lebron.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____________________________________________________________
    Hello! I'm Lebron
    What can I do for you?
    ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
