package lebron;

/**
 * Generic Task with a description and a done/undone state.
 * Its subclasses all make of this class's core features.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon for this task (X if done, blank otherwise).
     *
     * @return the status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the description of this task.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return the done/undone boolean.
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Returns a human-readable string for display.
     *
     * @return Human-readable string.
     */
    @Override
    public String toString() {
        return String.format("[%s] ", this.getStatusIcon()) + this.getDescription();
    }
}
