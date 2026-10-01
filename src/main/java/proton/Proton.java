package proton;

import proton.command.Command;
import proton.command.ExitCommand;
import proton.command.FindCommand;
import proton.command.ListCommand;
import proton.exception.ProtonException;
import proton.parser.Parser;
import proton.parser.Parser.ParsedCommand;
import proton.storage.Storage;
import proton.task.Task;
import proton.task.TaskList;
import proton.ui.Ui;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Runs the Proton chatbot and manages the user's task list.
 */
public class Proton {
    private final Parser parser = new Parser();
    private final Ui ui = new Ui();
    private final Storage storage = new Storage(Path.of("data", "proton.txt"));
    private TaskList tasks = new TaskList();

    /**
     * Starts Proton and processes commands from the standard input stream.
     *
     * @param args Command-line arguments, which Proton does not use.
     */
    public static void main(String[] args) {
        new Proton().run();
    }

    /**
     * Loads saved tasks, displays the greeting, and processes commands until exit or end of input.
     */
    private void run() {
        try (ui) {
            try {
                tasks = new TaskList(storage.load());
            } catch (IOException | IllegalArgumentException exception) {
                ui.showWelcome();
                ui.showLoadingError();
                return;
            }
            ui.showWelcome();

            boolean shouldContinue = true;
            while (shouldContinue && ui.hasNextCommand()) {
                String inputCommand = ui.readCommand();

                ui.showSeparator();
                shouldContinue = processCommand(inputCommand);
                ui.showSeparator();
            }
        }
    }

    /**
     * Restores both list membership and completion flags when a command cannot be saved.
     */
    private boolean processCommand(String inputCommand) {
        TaskList.Snapshot previousTasks = tasks.createSnapshot();

        try {
            return executeCommand(parser.parse(inputCommand));
        } catch (ProtonException exception) {
            tasks.restore(previousTasks);
            ui.showError(exception.getMessage());
            return true;
        }
    }

    /**
     * Dispatches a parsed operation to the corresponding application behavior.
     *
     * @return {@code false} when the command requests exit; {@code true} otherwise.
     * @throws ProtonException If the requested operation cannot be completed.
     */
    private boolean executeCommand(ParsedCommand command) throws ProtonException {
        switch (command.getType()) {
        case BYE:
            return executeCommand(new ExitCommand());
        case LIST:
            return executeCommand(new ListCommand());
        case FIND:
            return executeCommand(new FindCommand(command.getKeyword()));
        case MARK:
            markTask(command.getTaskNumber());
            break;
        case UNMARK:
            unmarkTask(command.getTaskNumber());
            break;
        case DELETE:
            deleteTask(command.getTaskNumber());
            break;
        case ADD:
            addTask(command.getTask());
            break;
        default:
            throw new IllegalStateException("Unhandled command type: " + command.getType());
        }
        return true;
    }

    /**
     * Runs an extracted command and translates its exit request for the loop.
     */
    private boolean executeCommand(Command command) throws ProtonException {
        command.execute(tasks, ui, storage);
        return !command.isExit();
    }

    /**
     * Marks and saves the selected task before displaying confirmation.
     *
     * @throws ProtonException If the task does not exist or the change cannot be saved.
     */
    private void markTask(int taskNumber) throws ProtonException {
        Task task = tasks.mark(taskNumber);
        saveTasks();
        ui.showTaskMarked(task);
    }

    /**
     * Unmarks and saves the selected task before displaying confirmation.
     *
     * @throws ProtonException If the task does not exist or the change cannot be saved.
     */
    private void unmarkTask(int taskNumber) throws ProtonException {
        Task task = tasks.unmark(taskNumber);
        saveTasks();
        ui.showTaskUnmarked(task);
    }

    /**
     * Removes the selected task and reports its details and the remaining count.
     *
     * @throws ProtonException If the command does not identify an existing task.
     */
    private void deleteTask(int taskNumber) throws ProtonException {
        Task removedTask = tasks.delete(taskNumber);
        saveTasks();
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Adds and saves a task before displaying confirmation and the updated count.
     *
     * @throws ProtonException If the change cannot be saved.
     */
    private void addTask(Task task) throws ProtonException {
        tasks.add(task);
        saveTasks();

        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Saves the current list without truncating the previous file on a failed write.
     *
     * @throws ProtonException If storage fails; the command handler restores the previous list.
     */
    private void saveTasks() throws ProtonException {
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            throw new ProtonException("Could not save data/proton.txt. No tasks were changed. "
                    + "Check the path and permissions, then retry or restart.");
        }
    }
}
