import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks and provides operations for accessing and changing the list.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks the tasks to store
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return true if there are no tasks
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of stored tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index the zero-based task position
     * @return the task at the requested position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index the zero-based task position
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a copy of the tasks for operations such as saving them to disk.
     *
     * @return a copy of the stored tasks
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Returns the tasks whose descriptions contain the supplied keyword.
     *
     * @param keyword the text to search for
     * @return the matching tasks, in their original list order
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.containsKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Converts a user-entered task number into a validated zero-based index.
     *
     * @param taskNumber the one-based task number entered by the user
     * @param command the command that requires the task number
     * @return the corresponding zero-based index
     * @throws YodaException if the task number is missing or out of range
     */
    public int getTaskIndex(String taskNumber, String command) throws YodaException {
        if (taskNumber.isEmpty()) {
            throw new YodaException("Missing task number!\n"
                    + "  Correct format: " + command + " <number>\n"
                    + "  Example:        " + command + " 2");
        }

        int taskIndex = Integer.parseInt(taskNumber) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new YodaException("Task " + (taskIndex + 1) + " does not exist.\n"
                    + "  You currently have " + tasks.size()
                    + " task(s). Please enter a number between 1 and " + tasks.size() + ".");
        }
        return taskIndex;
    }
}
