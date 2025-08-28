package lebron;

/**
 * A simple task with only a description and no associated date/time.
 */
public class Todo extends Task {

    /**
     * Creates an instance of a task with no specified deadline or any associated date/time.
     *
     * @param description description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a human-readable string for display.
     *
     * @return Human-readable string.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
