package duke;

import java.util.ArrayList;

/**
 * Represents the list of tasks Duke is tracking, with operations
 * to add, delete, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    /**
     * Creates a task list pre-populated with the given tasks.
     *
     * @param tasks The tasks to start with, e.g. loaded from disk.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }
    /**
     * Removes and returns the task at the given index.
     *
     * @param index The 0-based index of the task to remove.
     * @return The removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }
    /**
     * Returns the task at the given index.
     *
     * @param index The 0-based index of the task.
     * @return The task at that index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }
    /**
     * Returns the number of tasks in the list.
     *
     * @return The task count.
     */
    public int size() {
        return tasks.size();
    }
    /**
     * Returns the full underlying list of tasks.
     *
     * @return All tasks currently in the list.
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }
    /**
     * Finds all tasks whose description contains the given keyword.
     *
     * @param k The keyword to search for.
     * @return The tasks whose description contains the keyword.
     */
    public ArrayList<Task> find(String k) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().contains(k)) {
                matches.add(t);
            }
        }
        return matches;
    }
}
