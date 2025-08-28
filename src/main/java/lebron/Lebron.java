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
    public void run() {
        ui.greeting();
        boolean isExit = false;

        while (!isExit) {
            try {
                String command = ui.readCommand();
                if (command == null) {
                    break;
                }
                Parser.ParsedCommand pc = Parser.parse(command);

                switch (pc.type) {
                case BYE:
                    isExit = true;
                    ui.bye();
                    break;
                case CHECK:
                    ui.handleCheck(taskList, pc.arg1);
                    break;
                case LIST: {
                    ui.handleList(taskList);
                    break;
                }
                case FIND: {
                    ui.handleFind(taskList, pc.arg1);
                    break;
                }
                case MARK: {
                    Task t = taskList.mark(pc.index);
                    ui.showMarked(t);
                    storage.saveTasks(taskList.all());
                    break;
                }
                case UNMARK: {
                    Task t = taskList.unmark(pc.index);
                    ui.showUnmarked(t);
                    storage.saveTasks(taskList.all());
                    break;
                }
                case DELETE: {
                    Task removed = taskList.delete(pc.index);
                    ui.showDeleted(removed, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case TODO: {
                    Task t = new Todo(pc.arg1);
                    taskList.add(t);
                    ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case DEADLINE: {
                    Task t = new Deadline(pc.arg1, pc.arg2);
                    taskList.add(t);
                    ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                case EVENT: {
                    Task t = new Event(pc.arg1, pc.arg2, pc.arg3);
                    taskList.add(t);
                    ui.showAdded(t, taskList.size());
                    storage.saveTasks(taskList.all());
                    break;
                }
                default: throw new LebronException("Error - What talking you?");
                }
            } catch (LebronException e) {
                ui.showError(e.getMessage());
            }

        }
    }

    /**
     * Program's main.
     *
     * @param args args.
     */
    public static void main(String[] args) {
        new Lebron("./data/Lebron.txt").run();
    }
}

