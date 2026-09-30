package twitchchat;

import java.util.List;

import twitchchat.commands.Command;
import twitchchat.exceptions.TwitchChatCommandException;
import twitchchat.storage.Storage;
import twitchchat.tasks.Deadline;
import twitchchat.tasks.Event;
import twitchchat.tasks.Task;
import twitchchat.tasks.TaskList;
import twitchchat.tasks.Todo;

/**
 * Executes parsed commands and updates the task list.
 */
public class CommandHandler {

    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;

    /**
     * Creates a command handler with the task list and UI it should use.
     *
     * @param tasks task list to update
     * @param ui UI used to display command results
     */
    public CommandHandler(TaskList tasks, Ui ui) {
        this(tasks, ui, new Storage());
    }

    /**
     * Creates a command handler with the task list, UI, and storage it should use.
     *
     * @param tasks task list to update
     * @param ui UI used to display command results
     * @param storage storage used to save task updates
     */
    public CommandHandler(TaskList tasks, Ui ui, Storage storage) {
        this.tasks = tasks;
        this.ui = ui;
        this.storage = storage;
    }

    /**
     * Executes a parsed command.
     *
     * @param command command to execute
     * @return true if the application should continue, or false after a bye command
     * @throws TwitchChatCommandException if the command cannot be executed
     */
    public boolean executeCommand(Command command) {
        String[] arguments = command.getArguments();
        int taskId = command.getId();
        switch (command.getType()) {
        case TODO:
            addTodo(arguments[0]);
            saveTasks();
            break;
        case MARK:
            handleMarkCommand(true, taskId);
            saveTasks();
            break;
        case UNMARK:
            handleMarkCommand(false, taskId);
            saveTasks();
            break;
        case EVENT:
            addEvent(arguments);
            saveTasks();
            break;
        case DEADLINE:
            addDeadline(arguments);
            saveTasks();
            break;
        case LIST:
            tasks.printTasks();
            break;
        case DELETE:
            deleteTask(taskId);
            saveTasks();
            break;
        case FIND:
            findTasks(arguments[0]);
            break;
        case HI:
            System.out.println("Hello!");
            break;
        case BYE:
            ui.showGoodbye();
            return false;
        }
        return true;
    }

    private void addTodo(String taskName) {
        addTask(new Todo(taskName));
    }

    private void addDeadline(String[] arguments) {
        addTask(new Deadline(arguments[0], arguments[1]));
    }

    private void addEvent(String[] arguments) {
        addTask(new Event(arguments[0], arguments[1], arguments[2]));
    }

    private void addTask(Task task) {
        tasks.addTask(task);
        ui.showAddedTask(task, tasks.getTaskCount());
    }

    private void deleteTask(int taskId) {
        Task deletedTask = tasks.getTask(taskId);
        tasks.removeTask(taskId);
        ui.showDeletedTask(deletedTask, tasks.getTaskCount());
    }

    private void handleMarkCommand(boolean isMarkingDone, int taskId) {
        if (isMarkingDone) {
            Task currentTask = tasks.markTask(taskId);
            ui.showTaskMarkedAsDone(currentTask);
        } else {
            Task currentTask = tasks.unmarkTask(taskId);
            ui.showTaskMarkedAsNotDone(currentTask);
        }
    }

    private void saveTasks() {
        storage.saveTasks(tasks.getTasks());
    }

    private void findTasks(String keyword) {
        List<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(matchingTasks, keyword);
    }
}
