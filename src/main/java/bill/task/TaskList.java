package bill.task;

import bill.exception.BillException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Owns Bill's tasks and the operations that change their contents or completion state.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list from tasks loaded from storage.
     *
     * @param initialTasks Tasks to copy into this list.
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns a read-only view so callers cannot change the list without using its operations.
     *
     * @return Tasks in their numbered order.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Current task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether there are no tasks.
     *
     * @return True when the list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Adds a task at the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Marks the task with a user-facing (one-based) number as done.
     *
     * @param taskNumber Number displayed by the list command.
     * @return The updated task.
     * @throws BillException If that task does not exist.
     */
    public Task mark(int taskNumber) throws BillException {
        Task task = tasks.get(indexOf(taskNumber, "mark"));
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task with a user-facing (one-based) number as not done.
     *
     * @param taskNumber Number displayed by the list command.
     * @return The updated task.
     * @throws BillException If that task does not exist.
     */
    public Task unmark(int taskNumber) throws BillException {
        Task task = tasks.get(indexOf(taskNumber, "unmark"));
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes the task with a user-facing (one-based) number.
     *
     * @param taskNumber Number displayed by the list command.
     * @return The removed task.
     * @throws BillException If that task does not exist.
     */
    public Task delete(int taskNumber) throws BillException {
        return tasks.remove(indexOf(taskNumber, "delete"));
    }

    /**
     * Counts completed tasks for the stats command.
     *
     * @return Number of completed tasks.
     */
    public int countCompleted() {
        int count = 0;
        for (Task task : tasks) {
            if (task.isDone()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Finds tasks whose descriptions contain the requested text, ignoring case.
     * The returned list is separate from Bill's numbered task list, so searching
     * never changes or reorders the stored tasks.
     *
     * @param keyword Text to search for in task descriptions.
     * @return Matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        String searchText = keyword.toLowerCase(Locale.ROOT);
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(searchText)) {
                matches.add(task);
            }
        }
        return List.copyOf(matches);
    }

    /**
     * Converts a displayed task number to a list index after validating it.
     */
    private int indexOf(int taskNumber, String command) throws BillException {
        if (tasks.isEmpty()) {
            throw new BillException("There are no tasks to " + command + " yet.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BillException("Task " + taskNumber + " does not exist. Choose a number from 1 to "
                    + tasks.size() + ".");
        }
        return taskNumber - 1;
    }
}
