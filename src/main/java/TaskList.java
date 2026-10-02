import java.util.ArrayList;

/**
 * Stores tasks and provides operations for managing them.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the specified tasks.
     *
     * @param tasks tasks to place in the list
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return tasks in this task list
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the number of tasks.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based task index
     * @return task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Marks the task with the specified one-based number as completed.
     *
     * @param taskNumber one-based task number
     * @return task that was marked as completed
     * @throws ExiaException if the task number does not exist
     */
    public Task markAsDone(int taskNumber) throws ExiaException {
        Task task = getByNumber(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Deletes the task with the specified one-based number.
     *
     * @param taskNumber one-based task number
     * @return deleted task
     * @throws ExiaException if the task number does not exist
     */
    public Task delete(int taskNumber) throws ExiaException {
        getByNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Finds tasks whose descriptions contain the specified keyword.
     *
     * @param keyword keyword to search for
     * @return task list containing matching tasks
     */
    public TaskList find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }

        return new TaskList(matchingTasks);
    }

    private Task getByNumber(int taskNumber) throws ExiaException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new ExiaException("That task number does not exist.");
        }

        return tasks.get(taskNumber - 1);
    }
}
