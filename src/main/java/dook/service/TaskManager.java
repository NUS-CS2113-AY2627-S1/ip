package dook.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dook.exception.UnknownTaskException;
import dook.exception.TaskListIndexOutOfBoundsException;
import dook.io.TaskFileIO;
import dook.parser.Parser;
import dook.task.DeadlineTask;
import dook.task.EventTask;
import dook.task.Task;
import dook.task.ToDoTask;

/**
 * Manages the modification and viewing of the task list.
 */
public class TaskManager {
    private static final List<Parser<Task>> PARSERS = List.of(
        ToDoTask::parse,
        DeadlineTask::parse,
        EventTask::parse
    );

    private List<Task> tasks;
    private TaskFileIO io;

    /**
     * Constructs a new TaskManager with the specified file manager.
     *
     * @param  io The file manager, for storing and loading with local files.
     */
    public TaskManager(TaskFileIO io) {
        this.io = io;
        tasks = io.restore();
    }

    /**
     * Parses a user input into a new task.
     * Inserts it to the back of the task list.
     *
     * @param  userQuery Trimmed string input.
     * @return           The newly created task.
     * @throws UnknownTaskException
     *         If parsing fails.
     */
    public Task addTask(String userQuery) {
        for (Parser<Task> parser : PARSERS) {
            Optional<Task> maybeTask = parser.parse(userQuery);
            if (maybeTask.isPresent()) {
                Task newTask = maybeTask.get();
                tasks.add(newTask);
                return newTask;
            }
        }
        throw new UnknownTaskException();
    }

    /**
     * Inserts a task at the specified position.
     *
     * @param taskIndex The position to be inserted.
     * @param task      The new task.
     */
    public void addTask(int taskIndex, Task task) {
        tasks.add(taskIndex, task);
    }

    /**
     * Adds multiple tasks to the end of the task list.
     *
     * @param tasks Multiple tasks to be inserted.
     */
    public void addTasks(Collection<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Retrieves the task at the specified position.
     *
     * @param  id The position in the task list.
     * @return    The task.
     * @throws TaskListIndexOutOfBoundsException
     *         If the specified position is invalid.
     */
    public Task getTask(int id) {
        try {
            return tasks.get(id);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListIndexOutOfBoundsException();
        }
    }

    /**
     * Returns tasks that occur on the given date, sorted by time.
     *
     * @param  date The date to filter by.
     * @return      The sorted list of tasks occurring on the date.
     */
    public List<Task> getTasksByDate(LocalDate date) {
        return tasks.stream()
                    .filter(task -> task.isOnDate(date))
                    .sorted()
                    .toList();
    }

    /**
     * Retrieves a copy the task list.
     *
     * @return The copy of task list in immutable list form.
     */
    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Removes the task at the specified position.
     *
     * @param  id The position in the task list.
     * @return    The task.
     * @throws TaskListIndexOutOfBoundsException
     *         If the specified position is invalid.
     */
    public Task deleteTask(int id) {
        try {
            return tasks.remove(id);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListIndexOutOfBoundsException();
        }
    }

    /**
     * Removes all tasks from the task list.
     */
    public void deleteAllTasks() {
        tasks.clear();
    }

    /**
     * Removes the last task from the task list.
     */
    public void deleteLastTask() {
        tasks.removeLast();
    }

    /**
     * Removes all expired tasks from the task list.
     */
    public void deleteExpiredTasks() {
        tasks.removeIf(task -> task.isExpired());
    }

    /**
     * Stores the task list to local file.
     */
    public void save() {
        io.serialise(tasks);
    }
}
