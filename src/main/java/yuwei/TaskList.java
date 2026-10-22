package yuwei;

import yuwei.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The list of tasks the user is tracking, with the operations the chatbot needs on it.
 *
 * <p>The underlying {@code ArrayList} is private, so other classes can only change the
 * list through the methods below. That lets this class check every task index in one
 * place instead of each caller having to remember to do it.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Creates a task list holding the given tasks, e.g. those loaded from the data file. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds {@code task} to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at {@code index} (0-based) and returns it.
     *
     * @throws YuWeiException if there is no task at that index
     */
    public Task delete(int index) throws YuWeiException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at {@code index} (0-based) as done and returns it.
     *
     * @throws YuWeiException if there is no task at that index
     */
    public Task mark(int index) throws YuWeiException {
        checkIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at {@code index} (0-based) as not done and returns it.
     *
     * @throws YuWeiException if there is no task at that index
     */
    public Task unmark(int index) throws YuWeiException {
        checkIndex(index);
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the tasks whose description contains {@code keyword}, ignoring upper/lower case.
     * The list itself is not changed.
     */
    public List<Task> find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view of the tasks, for displaying or saving them.
     * Callers cannot add or remove tasks through it, which keeps this class in control.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /** Throws if {@code index} does not refer to a task in the list. */
    private void checkIndex(int index) throws YuWeiException {
        if (index < 0 || index >= tasks.size()) {
            // Report the 1-based number the user typed, not the internal 0-based index.
            throw new YuWeiException("There is no task " + (index + 1) + " in your list.");
        }
    }
}
