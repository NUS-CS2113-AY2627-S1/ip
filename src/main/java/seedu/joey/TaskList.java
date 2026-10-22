package seedu.joey;


import java.util.ArrayList;
import seedu.joey.task.Task;
/**
 * Holds the list of tasks and provides operations to add, delete
 * and access them.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialised with the given tasks
     * (typically loaded from disk).
     *
     * @param initialTasks Tasks to populate the list with.
     */
    public TaskList(ArrayList<Task> initialTasks) {
        this.tasks = initialTasks;
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index Zero-based index of the task to remove.
     * @return The task that was removed.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Zero-based index of the task to fetch.
     * @return The task at that position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return The task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the backing list of tasks (used by Storage for saving).
     *
     * @return The underlying ArrayList of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns a new TaskList containing only tasks whose description
     * contains the given keyword (case-insensitive substring match).
     */
    public TaskList find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String needle = keyword.toLowerCase();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(needle)) {
                matches.add(t);
            }
        }
        return new TaskList(matches);
    }

}
