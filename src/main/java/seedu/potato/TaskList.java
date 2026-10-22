package seedu.potato;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks entered during the current application session.
 */
public class TaskList {
    private static final int INITIAL_CAPACITY = 100;

    private final ArrayList<Task> tasks = new ArrayList<>(INITIAL_CAPACITY);
    private final Storage storage;

    /**
     * Creates an empty task list that saves changes using the given storage.
     *
     * @param storage Storage used to save tasks.
     */
    public TaskList(Storage storage) {
        this.storage = storage;
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param storage Storage used to save later changes.
     * @param initialTasks Tasks to place in the list at startup.
     */
    public TaskList(Storage storage, List<Task> initialTasks) {
        this.storage = storage;
        tasks.addAll(initialTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param newTask Task to store.
     * @throws IOException If the task save file cannot be written.
     */
    public void add(Task newTask) throws IOException {
        tasks.add(newTask);
        storage.save(tasks);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of tasks in the list.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Deletes the task at the given one-based position.
     *
     * @param taskNumber One-based task number shown by the {@code list} command.
     * @return Task that was deleted.
     * @throws IndexOutOfBoundsException If the task number is not in the list.
     * @throws IOException If the task save file cannot be written.
     */
    public Task delete(int taskNumber) throws IOException {
        int taskIndex = taskNumber - 1;
        Task taskRemoved = tasks.remove(taskIndex);
        storage.save(tasks);
        return taskRemoved;
    }

    /**
     * Marks the task at the given one-based position as done.
     *
     * @param taskNumber One-based task number shown by the {@code list} command.
     * @return Task that was marked as done.
     * @throws IndexOutOfBoundsException If the task number is not in the list.
     * @throws IOException If the task save file cannot be written.
     */
    public Task markAsDone(int taskNumber) throws IOException {
        Task task = getTask(taskNumber);
        task.markAsDone();
        storage.save(tasks);
        return task;
    }

    /**
     * Marks the task at the given one-based position as not done.
     *
     * @param taskNumber One-based task number shown by the {@code list} command.
     * @return Task that was marked as not done.
     * @throws IndexOutOfBoundsException If the task number is not in the list.
     * @throws IOException If the task save file cannot be written.
     */
    public Task markAsNotDone(int taskNumber) throws IOException {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        storage.save(tasks);
        return task;
    }

    /**
     * Returns an unmodifiable snapshot of the stored tasks.
     *
     * @return Tasks in input order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Finds tasks whose descriptions contain the search text or whose stored dates match it.
     * A date match is attempted only when the search text uses a supported date format.
     *
     * @param searchText Text or date to find.
     * @return Temporary list of matching tasks in their original order.
     */
    public List<Task> find(String searchText) {
        List<Task> foundTasks = new ArrayList<>();
        LocalDate searchDate = parseSearchDate(searchText);

        for (Task task : tasks) {
            if (task.containsDescription(searchText)
                    || searchDate != null && task.containsDate(searchDate)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }

    /**
     * Parses a supported search date, or returns {@code null} when the search text is not a valid date.
     *
     * @param searchText Potential date text.
     * @return Parsed date, or {@code null} for a non-date search.
     */
    private LocalDate parseSearchDate(String searchText) {
        try {
            return DateParser.parse(searchText);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /**
     * Returns the task at the given one-based position.
     *
     * @param taskNumber One-based task number shown by the {@code list} command.
     * @return Task at the requested position.
     * @throws IndexOutOfBoundsException If the task number is not in the list.
     */
    private Task getTask(int taskNumber) {
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task number is outside the list");
        }
        return tasks.get(taskIndex);
    }
}
