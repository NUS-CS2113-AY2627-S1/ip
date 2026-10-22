import java.util.ArrayList;

/** Owns the collection of tasks and provides task-level operations. */
public class TaskList {
    private final Storage storage;
    private final ArrayList<Task> tasks;

    /** Creates an empty task list using the supplied persistence component. */
    public TaskList(Storage storage) {
        this.storage = storage;
        tasks = new ArrayList<>();
    }

    /** Adds a task and persists the updated list. */
    public void add(Task task) {
        tasks.add(task);
        storage.save(this, tasks.size());
    }

    /** Adds a task without saving, for use while loading persisted data. */
    public void addLoaded(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the zero-based index. */
    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        storage.save(this, tasks.size());
        return removedTask;
    }

    /** Marks the task at the zero-based index as complete. */
    public void mark(int index) {
        get(index).setDone(true);
        storage.save(this, tasks.size());
    }

    /** Marks the task at the zero-based index as incomplete. */
    public void unmark(int index) {
        get(index).setDone(false);
        storage.save(this, tasks.size());
    }

    /** Returns the task at the zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns tasks whose bodies contain the search term, preserving order. */
    public ArrayList<Task> find(String searchTerm) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.contains(searchTerm)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
