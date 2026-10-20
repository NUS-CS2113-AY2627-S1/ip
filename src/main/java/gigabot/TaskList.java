package gigabot;

import java.util.ArrayList;

/**
 * Represents the list of tasks and provides operations to manage them.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a new, empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList utilizing an existing ArrayList of tasks.
     *
     * @param tasks The initial ArrayList of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The task that was removed.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The requested task.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return The size of the list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Returns the underlying ArrayList of tasks.
     *
     * @return The ArrayList containing all current tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}