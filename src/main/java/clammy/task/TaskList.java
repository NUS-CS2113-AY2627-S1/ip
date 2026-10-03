package clammy.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import clammy.exception.TaskNotFoundException;

/**
 * Stores tasks and translates user-facing task numbers into list indexes.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task identified by a one-based task number.
     *
     * @param taskNumber Task number shown to the user.
     * @return Task with the supplied number.
     * @throws TaskNotFoundException If the number does not identify a stored task.
     */
    public Task getTask(int taskNumber) throws TaskNotFoundException {
        if (!hasTaskNumber(taskNumber)) {
            throw new TaskNotFoundException();
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns a task, shifting later tasks down by one position.
     *
     * @param taskNumber One-based task number shown to the user.
     * @return Task removed from the list.
     * @throws TaskNotFoundException If the number does not identify a stored task.
     */
    public Task removeTask(int taskNumber) throws TaskNotFoundException {
        if (!hasTaskNumber(taskNumber)) {
            throw new TaskNotFoundException();
        }
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns whether a one-based number identifies a stored task.
     */
    public boolean hasTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns the number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable view of tasks in insertion order.
     * Changes made through this TaskList remain visible in the returned view.
     * The tasks themselves are shared and can still have their completion status changed.
     *
     * @return Live list view that prevents callers from adding or removing tasks directly.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns a separate list of tasks whose descriptions contain the supplied text, ignoring case.
     * Matches retain insertion order and refer to the original tasks, including their completion status.
     * Matching treats the keyword as one literal substring: word order and internal spaces matter,
     * and dates and event times are not searched. The returned list captures membership at the time
     * of the search; later additions or removals in this list do not update that membership.
     *
     * @param keyword Nonblank substring validated by the parser, with surrounding whitespace removed.
     * @return Matching tasks, or an empty list if no descriptions match.
     */
    public TaskList findTasks(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        TaskList matches = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
