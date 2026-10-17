package noah;

import java.nio.file.Path;

import noah.exception.NoahException;
import noah.parser.Parser;
import noah.storage.Storage;
import noah.task.Task;
import noah.task.TaskList;
import noah.ui.Ui;

/**
 * Coordinates command parsing, task updates, console interaction, and storage.
 */
public class Noah {
    private static final Path DATA_FILE_PATH = Path.of("data", "noah.txt");

    private final Ui ui;
    private final Storage storage;

    /**
     * Duplicate addition awaiting confirmation; null when no addition is pending.
     */
    private Task pendingTask;

    /**
     * Creates Noah with a console user interface and task-file storage.
     */
    public Noah() {
        ui = new Ui();
        storage = new Storage(DATA_FILE_PATH);
    }

    /**
     * Starts the program and processes user commands until the user exits.
     *
     * @param args Command-line arguments; not used.
     */
    public static void main(String[] args) {
        new Noah().run();
    }

    /**
     * Loads saved tasks and processes commands until the user exits.
     */
    public void run() {
        ui.showWelcome();
        TaskList tasks = loadTasks();
        boolean isExit = false;

        while (!isExit) {
            String userCommand = ui.readCommand();
            ui.showLine();
            try {
                isExit = executeCommand(tasks, userCommand);
            } catch (NoahException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.close();
    }

    /**
     * Loads saved tasks, or starts with an empty list if loading fails.
     *
     * @return Tasks loaded successfully, or an empty list after an error.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (NoahException e) {
            ui.showError(e.getMessage());
            ui.showLine();
            return new TaskList();
        }
    }

    /**
     * Processes a command and saves tasks after an operation changes the list.
     *
     * @param tasks Tasks currently stored.
     * @param userCommand Full command entered by the user.
     * @return Whether the user requested to exit.
     * @throws NoahException If the command is invalid or saving fails.
     */
    private boolean executeCommand(TaskList tasks, String userCommand) throws NoahException {
        String command = Parser.parseCommandWord(userCommand);
        if (pendingTask != null && !command.equals("yes") && !command.equals("no")) {
            resolveDuplicate(tasks, false);
        }
        switch (command) {
        case "bye":
            ui.showGoodbye();
            return true;
        case "list":
            ui.showTaskList(tasks.getTasks());
            return false;
        case "find":
            ui.showMatchingTasks(tasks.find(Parser.parseFindKeyword(userCommand)));
            return false;
        case "todo":
        case "deadline":
        case "event":
            requestAddTask(tasks, Parser.parseTask(userCommand));
            return false;
        case "yes":
        case "no":
            resolveDuplicate(tasks, command.equals("yes"));
            return false;
        case "clear":
            int removedCount = tasks.clear();
            ui.showTasksCleared(removedCount);
            if (removedCount == 0) {
                return false;
            }
            break;
        case "delete":
            Task deletedTask = tasks.delete(Parser.parseTaskNumber(userCommand));
            ui.showTaskDeleted(deletedTask, tasks.size());
            break;
        case "mark":
            Task markedTask = tasks.mark(Parser.parseTaskNumber(userCommand));
            ui.showTaskMarked(markedTask);
            break;
        case "unmark":
            Task unmarkedTask = tasks.unmark(Parser.parseTaskNumber(userCommand));
            ui.showTaskUnmarked(unmarkedTask);
            break;
        default:
            throw new NoahException("That command isn't in my adventurer's handbook!\n"
                    + "Try: list, todo read book, clear, bye... :)");
        }
        storage.saveTasks(tasks.getTasks());
        return false;
    }

    /**
     * Adds a new task immediately or requests confirmation for matching details.
     * A pending duplicate is neither added to the list nor saved to the file.
     *
     * @param tasks Tasks currently stored.
     * @param task New task requested by the user.
     * @throws NoahException If saving a nonduplicate addition fails.
     */
    private void requestAddTask(TaskList tasks, Task task) throws NoahException {
        int duplicateNumber = tasks.findDuplicate(task);
        if (duplicateNumber > 0) {
            pendingTask = task;
            ui.showDuplicateTask(duplicateNumber, tasks.getTasks().get(duplicateNumber - 1));
            return;
        }
        addTask(tasks, task);
    }

    /**
     * Resolves the pending duplicate once, adding it only after an explicit yes.
     * A recognized nonconfirmation command also cancels the pending addition.
     *
     * @param tasks Tasks currently stored.
     * @param isConfirmed Whether the user chose to add another copy.
     * @throws NoahException If no addition is pending or saving the new copy fails.
     */
    private void resolveDuplicate(TaskList tasks, boolean isConfirmed) throws NoahException {
        if (pendingTask == null) {
            throw new NoahException("No duplicate task is waiting for a yes or no. What's our next quest?");
        }
        Task task = pendingTask;
        pendingTask = null;
        if (isConfirmed) {
            addTask(tasks, task);
        } else {
            ui.showAdditionCancelled();
        }
    }

    /**
     * Adds an accepted task, reports the result, and saves the updated list.
     *
     * @param tasks Tasks currently stored.
     * @param task Task whose addition is accepted.
     * @throws NoahException If the updated list cannot be saved.
     */
    private void addTask(TaskList tasks, Task task) throws NoahException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.saveTasks(tasks.getTasks());
    }
}
