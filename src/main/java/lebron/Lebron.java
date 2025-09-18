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
                reply = handleTaskCreation(t, pc);
                break;
            }
            case DEADLINE: {
                Task t = new Deadline(pc.getArg1(), pc.getArg2());
                reply = handleTaskCreation(t, pc);
                break;
            }
            case EVENT: {
                Task t = new Event(pc.getArg1(), pc.getArg2(), pc.getArg3());
                reply = handleTaskCreation(t, pc);
                break;
            }
            case UNDO: {
                reply = handleUndo();
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
     * Handles task creation operations (TODO, DEADLINE, EVENT).
     * 
     * @param task the task to add
     * @param command the parsed command for history tracking
     * @return the response message
     */
    private String handleTaskCreation(Task task, Parser.ParsedCommand command) throws LebronException {
        taskList.add(task);
        String reply = ui.showAdded(task, taskList.size());
        storage.saveTasks(taskList.all());
        history.add(command);
        return reply;
    }
    
    /**
     * Handles undo operations by reverting the last command.
     * 
     * @return the response message
     */
    private String handleUndo() {
        try {
            Parser.ParsedCommand lastCommand = history.pop();
            switch (lastCommand.getType()) {
            case MARK: {
                Task t = taskList.unmark(lastCommand.getIndex());
                String reply = ui.showUnmarked(t);
                storage.saveTasks(taskList.all());
                return reply;
            }
            case UNMARK: {
                Task t = taskList.mark(lastCommand.getIndex());
                String reply = ui.showMarked(t);
                storage.saveTasks(taskList.all());
                return reply;
            }
            case DELETE: {
                Task t = deletedTasks.pop();
                taskList.add(t);
                String reply = ui.showAdded(t, taskList.size());
                storage.saveTasks(taskList.all());
                return reply;
            }
            case EVENT: case DEADLINE: case TODO: {
                Task removed = taskList.delete(taskList.size());
                String reply = ui.showDeleted(removed, taskList.size());
                storage.saveTasks(taskList.all());
                return reply;
            }
            default: {
                throw new LebronException("Error - Nothing to undo");
            }
            }
        } catch (EmptyStackException | LebronException e) {
            return ui.showError("Error - Nothing to undo.");
        }
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

