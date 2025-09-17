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

## Features
- [x] Keep track of your to-dos, deadlines, and events
- [x] Persistent storage of tasks between runs
- [x] Mark/Unmark tasks as done
- [x] Delete tasks
- [x] List all tasks
- [x] Find tasks by keyword
- [x] Check tasks occurring on a specific date
- [x] Undo the last change (where applicable)
---
## Commands
Lebron understands the following commands:

- **hi**
  - Greets you.

- **bye**
  - Exits the app (with a short delay so you can read the final reply).

- **list**
  - Lists all tasks with their indices.

- **todo \<description>**
  - Adds a Todo task.
  - Example: `todo read book`

- **deadline \<description> /by \<yyyy-MM-dd>**
  - Adds a Deadline task with a due date.
  - Example: `deadline return book /by 2025-10-01`

- **event \<description> /from \<start> /to <end>**
  - Adds an Event with a start and end.
  - Example: `event team meeting /from 2025-10-01 10:00 /to 2025-10-01 11:00`

- **mark \<index>**
  - Marks the specified task as done.
  - Example: `mark 2`

- **unmark \<index>**
  - Marks the specified task as not done.
  - Example: `unmark 2`

- **delete \<index>**
  - Deletes the specified task.
  - Example: `delete 3`

- **find \<keyword>**
  - Shows tasks whose descriptions contain the keyword (case-insensitive).
  - Example: `find book`

- **check \<yyyy-MM-dd>**
  - Lists tasks scheduled on a specific date (deadlines due that day and events occurring that day).
  - Example: `check 2025-10-01`

- **undo**
  - Reverts the last change (e.g., add/delete/mark actions where supported).


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
**Acknowledgments**

Used Cursor to implement some UI changes, particularly seen in 'branch-A-BetterGUI'.
