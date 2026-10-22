package isa.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stores and provides access to the user's tasks.
 */
public class TaskList {

    private final ArrayList<Task> tasks = new ArrayList<>();


    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Finds tasks whose descriptions contain a keyword, ignoring case.
     *
     * @param keyword Text to find in task descriptions.
     * @return Zero-based indices of matching tasks in the full list.
     */
    public List<Integer> findMatchingIndices(String keyword) {
        List<Integer> matches = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (int i = 0; i < tasks.size(); i++) {
            String description = tasks.get(i).getDescription().toLowerCase(Locale.ROOT);

            if (description.contains(normalizedKeyword)) {
                matches.add(i);
            }
        }

        return matches;
    }
}
