package lumi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lumi.task.Task;

/**
 * Holds the tasks Lumi is tracking. Every access by position goes through this
 * class, so the bounds check and the message explaining a bad task number live
 * in one place rather than being repeated at each call site.
 */
public class TaskList {
    /** Task numbers shown to the user start at 1, whereas list indexes start at 0. */
    public static final int FIRST_TASK_NUMBER = 1;

    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Returns how many tasks the list holds. */
    public int size() {
        return tasks.size();
    }

    /** Returns true when there is nothing in the list. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Appends one task to the end of the list.
     *
     * @param task The task to record.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Appends tasks restored from a previous run. */
    public void addAll(List<Task> restored) {
        tasks.addAll(restored);
    }

    /**
     * Returns the task at the given position.
     *
     * @throws LumiException If no task sits at that position.
     */
    public Task get(int index) throws LumiException {
        requireInRange(index);
        return tasks.get(index);
    }

    /**
     * Removes the task at the given position and returns it.
     *
     * @throws LumiException If no task sits at that position.
     */
    public Task remove(int index) throws LumiException {
        requireInRange(index);
        return tasks.remove(index);
    }

    /**
     * Returns the tasks whose description mentions the given text, keeping the
     * order they appear in the list.
     *
     * @param keyword Text to look for.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.hasKeyword(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /** Returns a read-only view, for listing the tasks and for saving them. */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Rejects an index that names no task, explaining how many there are so the
     * user can see what went wrong.
     *
     * @param index Zero-based index to check.
     * @throws LumiException If the index falls outside the list.
     */
    private void requireInRange(int index) throws LumiException {
        if (index < 0 || index >= tasks.size()) {
            throw new LumiException("You have " + tasks.size() + " tasks, so there is no task "
                    + (index + FIRST_TASK_NUMBER) + ".");
        }
    }
}
