package jamal;

import java.util.ArrayList;
import java.util.List;

import jamal.task.Task;

/**
 * Holds the tasks in the order they were added and provides the operations
 * the commands need. Index checks live here so every command reports a
 * missing task with the same message.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list holding the given tasks, e.g. ones loaded from disk.
     *
     * @param tasks Initial tasks, in order.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Zero-based index.
     * @return The task at that index.
     * @throws JamaLException If no task exists at that index.
     */
    public Task get(int index) throws JamaLException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given index.
     * Later tasks shift down, so numbering stays continuous.
     *
     * @param index Zero-based index.
     * @return The removed task.
     * @throws JamaLException If no task exists at that index.
     */
    public Task remove(int index) throws JamaLException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks.
     *
     * @return Number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return True if the list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the tasks as a read-only list, e.g. for saving to disk.
     *
     * @return Unmodifiable view of the tasks.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /**
     * Returns every task whose description contains the keyword, ignoring case.
     *
     * @param keyword Text to look for.
     * @return Matching tasks, in list order.
     */
    public List<Task> find(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the number of tasks with the right singular/plural noun,
     * e.g. "1 task" or "3 tasks".
     *
     * @return Task count with its noun.
     */
    public String countText() {
        int count = tasks.size();
        return count + (count == 1 ? " task" : " tasks");
    }

    private void checkIndex(int index) throws JamaLException {
        if (index < 0 || index >= tasks.size()) {
            throw new JamaLException("no task " + (index + 1) + ".");
        }
    }
}
