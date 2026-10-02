package nova;

import java.util.ArrayList;
import java.util.List;

import nova.task.Task;

/**
 * Holds user's tasks and provides operations Nova needs to do on them
 * (like the remaining program doesn't depend on how tasks are stored internally, and
 * index validation is done in only one place
 **/

public class TaskList {

    private final List<Task> tasks;

    /**
     * Creates empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list pre-filled with the given tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * throws NovaException if the index is out of range.
     */
    public Task delete(int index) throws NovaException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given zero-based index
     * throws NovaException if index is out of range.
     */
    public Task get(int index) throws NovaException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Returns the tasks whose description contains the given keyword.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns true if the list contains no tasks.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only view of the tasks, e.g. for saving or listing.
     */
    public List<Task> getAll() {
        return List.copyOf(tasks);
    }

    /**
     * Sees that the given zero-based index points to a task that actually exists
     */
    private void checkIndex(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException(
                    "Task " + (index + 1) + "? Bold of you to assume that exists. "
                    + "You have " + tasks.size() + " task/s.");
        }
    }
}