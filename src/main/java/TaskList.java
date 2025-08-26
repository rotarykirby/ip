import java.util.List;
import java.util.ArrayList;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }


    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task delete(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        return tasks.remove(idx);
    }

    public Task mark(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        Task t = tasks.get(idx);
        t.markDone();
        return t;
    }

    public Task unmark(int idx) throws LebronException {
        --idx;
        if (idx < 0 || idx >= tasks.size()) throw new LebronException("Error - index out of range.");
        Task t = tasks.get(idx);
        t.markUndone();
        return t;
    }

    public List<Task> all() {
        return tasks;
    }
}
