package slowbro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Represents the collection of tasks managed by the application. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Creates a task list containing the given tasks. */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /** Adds a task to this list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Deletes the task at the given zero-based index. */
    public void delete(int index) {
        tasks.remove(index);
    }

    /** Marks the task at the given zero-based index as done. */
    public void mark(int index) {
        get(index).markAsDone();
    }

    /** Marks the task at the given zero-based index as not done. */
    public void unmark(int index) {
        get(index).unmarkAsDone();
    }

    /** Returns the task at the given zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Returns a copy of the tasks for persistence. */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     *
     * @param keyword the keyword to search for
     * @return matching tasks in their original order
     */
    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT)
                        .contains(normalizedKeyword))
                .toList();
    }

}
