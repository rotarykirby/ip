package lebron;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Class that combines UI, Storage and TaskList.
 */
public class Lebron {

    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;
    private Stack<Parser.ParsedCommand> history = new Stack<>();
    private Stack<Task> deletedTasks = new Stack<>();

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
        assert ui != null && storage != null && taskList != null : "Core components must be initialised by now";
    }

    /**
     * Shows user a greeting, before waiting for user's commands. Runs a loop that performs tasks based on the user's
     * input. Loop breaks when user types "bye", then shows user a goodbye message before exiting.
     *
     * @param input user's input.
     * @return Lebron's reply.
     */
    public String run(String input) {
        String reply = "";
        try {
            String command = input.trim();
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
                history.add(pc);
                break;
            }
            case UNMARK: {
                Task t = taskList.unmark(pc.getIndex());
                reply = ui.showUnmarked(t);
                storage.saveTasks(taskList.all());
                history.add(pc);
                break;
            }
            case DELETE: {
                Task removed = taskList.delete(pc.getIndex());
                this.deletedTasks.add(removed);
                reply = ui.showDeleted(removed, taskList.size());
                storage.saveTasks(taskList.all());
                history.add(pc);
                break;
            }
            case TODO: {
                Task t = new Todo(pc.getArg1());
                taskList.add(t);
                reply = ui.showAdded(t, taskList.size());
                storage.saveTasks(taskList.all());
                history.add(pc);
                break;
            }
            case DEADLINE: {
                Task t = new Deadline(pc.getArg1(), pc.getArg2());
                taskList.add(t);
                reply = ui.showAdded(t, taskList.size());
                storage.saveTasks(taskList.all());
                history.add(pc);
                break;
            }
            case EVENT: {
                Task t = new Event(pc.getArg1(), pc.getArg2(), pc.getArg3());
                taskList.add(t);
                reply = ui.showAdded(t, taskList.size());
                storage.saveTasks(taskList.all());
                history.add(pc);
                break;
            }
            case UNDO: {
                try {
                    Parser.ParsedCommand lastCommand = history.pop();
                    switch (lastCommand.getType()) {
                    case MARK: {
                        Task t = taskList.unmark(lastCommand.getIndex());
                        reply = ui.showUnmarked(t);
                        storage.saveTasks(taskList.all());
                        break;
                    }
                    case UNMARK: {
                        Task t = taskList.mark(lastCommand.getIndex());
                        reply = ui.showMarked(t);
                        storage.saveTasks(taskList.all());
                        break;
                    }
                    case DELETE: {
                        Task t = deletedTasks.pop();
                        taskList.add(t);
                        reply = ui.showAdded(t, taskList.size());
                        storage.saveTasks(taskList.all());
                        break;
                    }
                    case EVENT: case DEADLINE: case TODO: {
                        taskList.size();
                        Task removed = taskList.delete(taskList.size());
                        reply = ui.showDeleted(removed, taskList.size());
                        storage.saveTasks(taskList.all());
                        break;
                    }
                    // should not reach here because of earlier try catch statement
                    default: {
                        throw new LebronException("Error - Nothing to undo");
                    }
                    }
                } catch (EmptyStackException e) {
                    reply = ui.showError("Error - Nothing to undo.");
                }
                break;
            }
            // there should not be a default because the commands have already been filtered through Parser
            default: {
                throw new LebronException("Error - Lebron does not know what you are talking about.");
            }
            }
        } catch (LebronException e) {
            reply = ui.showError(e.getMessage());
        }
        return reply;
    }

    /**
     * Simply passes the user input into the run command and gets a response
     * Makes code more readable from other file
     *
     * @param input user's input
     * @return Lebron's reply
     */
    public String getResponse(String input) {
        return this.run(input);
    }
}

