package lebron;

import java.util.List;

/**
 * Main class that combines UI, Storage and TaskList.
 */
public class Lebron {

    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    /**
     * Constructs the program, wiring the UI and storage, load any previously saved tasks from the given file path.
     *
     * @param filePath path to the save file.
     */
    public Lebron(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        TaskList temp;

        try {
            List<Task> loaded = storage.loadTasks();
            temp = new TaskList(loaded);
        } catch (LebronException e) {
            ui.showLoadingError(e.getMessage());
            temp = new TaskList();
        }

        this.taskList = temp;
    }

    /**
     * Shows user a greeting, before waiting for user's commands. Runs a loop that performs tasks based on the user's
     * input. Loop breaks when user types "bye", then shows user a goodbye message before exiting.
     */
    public String run(String input) {
        // ui.greeting();
        String reply = "";

            try {
                String command = input.trim();
                if (command == null) {
                    throw new LebronException("Error - Unknown Error");
                }
                Parser.ParsedCommand pc = Parser.parse(command);

                switch (pc.getType()) {
                case HI:
                    reply = ui.greeting();
                    break;
                case BYE:
                    reply = ui.bye();
                    break;
                case CHECK:
                    reply = ui.handleCheck(taskList, pc.getArg1());
                    break;
                case LIST: {
                    reply = ui.handleList(taskList);
                    break;
                }
                case FIND: {
                    reply = ui.handleFind(taskList, pc.getArg1());
                    break;
                }
                case MARK: {
                    Task t = taskList.mark(pc.getIndex());
                    reply = ui.showMarked(t);
                    storage.saveTasks(taskList.all());
                    break;
                }
                case UNMARK: {
                    Task t = taskList.unmark(pc.getIndex());
                    reply = ui.showUnmarked(t);
                    storage.saveTasks(taskList.all());
                    break;
                }
                case DELETE: {
                    Task removed = taskList.delete(pc.getIndex());
                    reply = ui.showDeleted(removed, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case TODO: {
                    Task t = new Todo(pc.getArg1());
                    taskList.add(t);
                    reply = ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case DEADLINE: {
                    Task t = new Deadline(pc.getArg1(), pc.getArg1());
                    taskList.add(t);
                    reply = ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case EVENT: {
                    Task t = new Event(pc.getArg1(), pc.getArg2(), pc.getArg3());
                    taskList.add(t);
                    reply = ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                default: throw new LebronException("Error - What talking you?");
                }
            } catch (LebronException e) {
                reply = ui.showError(e.getMessage());
            }

        return reply;
    }

    public String getResponse(String input) {
        return this.run(input);
    }
}

