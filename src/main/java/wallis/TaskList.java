package wallis;

import java.util.ArrayList;

/**
 * Represents the list of tasks and provides operations to manage and modify the list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList loaded with an existing ArrayList of tasks.
     *
     * @param tasks The pre-existing ArrayList of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The removed task.
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index without removing it.
     *
     * @param index The zero-based index of the task.
     * @return The requested task.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Gets the current number of tasks in the list.
     *
     * @return The total number of tasks.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Retrieves the raw ArrayList containing all tasks.
     *
     * @return The ArrayList of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}