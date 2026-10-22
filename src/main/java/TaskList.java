import java.util.ArrayList;

/**
 * Represents the list of tasks and provides operations to add, remove, and query tasks.
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
     * Creates a task list using loaded tasks, or an empty list when none are supplied.
     *
     * @param tasks tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Adds a task at the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at the specified position.
     *
     * @param index zero-based insertion position
     * @param task task to insert
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Removes and returns the task at the specified position.
     *
     * @param index zero-based task position
     * @return removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified position.
     *
     * @param index zero-based task position
     * @return task at the position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks managed by this list for storage operations.
     *
     * @return underlying list of tasks
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public TaskList findMatchingTasks(String keyword) {
        ArrayList<Task> matchedTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchedTasks.add(task);
            }
        }
        return new TaskList(matchedTasks);
    }

}

