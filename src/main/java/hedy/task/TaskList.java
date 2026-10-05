package hedy.task;

import java.time.LocalDate;
import java.util.ArrayList;

import hedy.exception.HedyException;

/**
 * The list of tasks Hedy is tracking.
 * Task numbers shown to the user start at 1.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from tasks that were loaded from disk.
     *
     * @param loaded tasks already read from the save file
     */
    public TaskList(ArrayList<Task> loaded) {
        this.tasks = loaded;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to store
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task with the given user-facing number.
     *
     * @param taskNumber number shown by {@code list}, starting at 1
     * @return the task that was removed
     * @throws HedyException if that number is not in the list
     */
    public Task delete(int taskNumber) throws HedyException {
        return tasks.remove(toIndex(taskNumber, "delete"));
    }

    /**
     * Marks or unmarks the task with the given user-facing number.
     *
     * @param taskNumber number shown by {@code list}, starting at 1
     * @param isDone true to mark the task done
     * @return the task after the change
     * @throws HedyException if that number is not in the list
     */
    public Task mark(int taskNumber, boolean isDone) throws HedyException {
        Task task = tasks.get(toIndex(taskNumber, isDone ? "mark" : "unmark"));
        task.setDone(isDone);
        return task;
    }

    /**
     * Returns tasks whose description contains the keyword.
     * Matching ignores capital letters.
     *
     * @param keyword text to look for
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String needle = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(needle)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns deadlines due on the given date.
     * Each result keeps the number {@code list} would show, so the user can mark or delete it.
     *
     * @param date the day to match against each deadline
     */
    public ArrayList<NumberedTask> deadlinesOn(LocalDate date) {
        ArrayList<NumberedTask> matches = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task instanceof Deadline deadline && deadline.getBy().equals(date)) {
                matches.add(new NumberedTask(i + 1, task));
            }
        }
        return matches;
    }

    /**
     * Returns the tasks in list order, for saving and for {@code list}.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /** Returns how many tasks are stored. */
    public int size() {
        return tasks.size();
    }

    /**
     * Converts a user-facing task number into a list index.
     *
     * @param taskNumber number the user typed, starting at 1
     * @param commandName command to name in the error, such as {@code delete}
     */
    private int toIndex(int taskNumber, String commandName) throws HedyException {
        if (tasks.isEmpty()) {
            throw new HedyException("OOPS!!! Your list is empty, so there is no task " + taskNumber + " to "
                    + commandName + ".");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new HedyException("OOPS!!! There is no task " + taskNumber + ". "
                    + "Use list to see tasks numbered 1 to " + tasks.size() + ".");
        }
        return taskNumber - 1;
    }
}
