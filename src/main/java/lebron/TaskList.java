package lebron;

import java.util.List;
import java.util.ArrayList;

/**
 * Container for a list of tasks.
 * Provides operations to edit the list, such as: add, mark, unmark and delete.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list instance with an empty array list if no list of tasks is specified.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list instance with the specified list of tasks.
     *
     * @param tasks list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given 1-based index.
     *
     * @param idx the index of the task.
     * @return the task.
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Adds a task to the list.
     *
     * @param t the task.
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Deletes the task at the given 1-based index.
     *
     * @param idx the index of the task.
     * @return the task.
     * @throws LebronException if indexing error occurs.
     */
    public Task delete(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        return tasks.remove(idx);
    }

    /**
     * Marks the task at the given 1-based index as done.
     *
     * @param idx the index of the task.
     * @return the task.
     * @throws LebronException if indexing error occurs.
     */
    public Task mark(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        Task t = tasks.get(idx);
        t.markDone();
        return t;
    }

    /**
     * Marks the task at the given 1-based index as not done.
     *
     * @param idx the index of the task.
     * @return the task.
     * @throws LebronException if indexing error occurs.
     */
    public Task unmark(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        Task t = tasks.get(idx);
        t.markUndone();
        return t;
    }

    /**
     * Returns the backing list of all the tasks.
     *
     * @return the list of all the tasks.
     */
    public List<Task> all() {
        return tasks;
    }
}
