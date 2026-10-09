package nimbus;

import java.util.ArrayList;

/**
 * Manages the tasks stored by Nimbus.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing previously loaded tasks.
     *
     * @param tasks Tasks loaded from storage.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Saves the current tasks using Storage.
     */
    public void save() {
        Storage.saveTasks(tasks);
    }
}