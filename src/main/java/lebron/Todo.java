package lebron;

/**
 * A simple task with only a description and no associated date/time.
 */
public class Todo extends Task {

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
