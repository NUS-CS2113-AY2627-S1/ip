package neo;

import java.util.ArrayList;
import neo.task.Task;

/**
 * Represents the list of tasks in the application.
 * Provides operations to add, delete, and retrieve tasks from the list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs a TaskList using an existing list of tasks.
     *
     * @param tasks An {@code ArrayList<Task>} containing the initial tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the list.
     *
     * @param task The {@code Task} to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to be removed.
     * @return The {@code Task} that was removed from the list.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The zero-based index of the target task.
     * @return The {@code Task} at the specified index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return The total number of tasks as an integer.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Retrieves the entire list of tasks.
     *
     * @return An {@code ArrayList<Task>} containing all current tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds and returns a list of tasks that contain the given keyword.
     *
     * @param keyword The search term to match against task descriptions.
     * @return An ArrayList of matching tasks.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}