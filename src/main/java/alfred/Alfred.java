package alfred;

import java.io.IOException;
import java.nio.file.Path;

import alfred.exception.AlfredException;
import alfred.task.Task;

/**
 * Runs the Alfred command-line chatbot.
 */
public class Alfred {

    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Alfred and loads tasks from the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Alfred(String filePath) {
        this.parser = new Parser();
        this.storage = new Storage(Path.of(filePath));
        this.ui = new Ui();

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load(ui::showMessage));
        } catch (IOException exception) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Starts Alfred and processes commands until the user exits.
     */
    public void run() {
        ui.showWelcomeMessage();

        try (ui) {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand().trim();
                ui.showDivider();

                try {
                    Parser.CommandType commandType = parser.parseCommandType(command);
                    if (commandType == Parser.CommandType.BYE) {
                        ui.showGoodbyeMessage();
                        break;
                    }
                    processCommand(command, commandType);
                } catch (AlfredException exception) {
                    ui.showMessage(exception.getMessage());
                } catch (IOException exception) {
                    ui.showSavingError();
                }

                ui.showDivider();
            }
        }
    }

    /**
     * Starts Alfred using the default data file.
     *
     * @param args Command-line arguments. Alfred does not use them.
     */
    public static void main(String[] args) {
        new Alfred("data/alfred.txt").run();
    }

    /**
     * Performs the operation represented by a parsed command type.
     *
     * @param command Full command entered by the user.
     * @param commandType Type of the command.
     */
    private void processCommand(String command, Parser.CommandType commandType)
            throws AlfredException, IOException {
        switch (commandType) {
            case LIST -> ui.showTaskList(tasks);
            case DELETE -> deleteTask(parser.parseTaskNumber(command, commandType));
            case UNMARK -> updateTaskStatus(parser.parseTaskNumber(command, commandType), false);
            case MARK -> updateTaskStatus(parser.parseTaskNumber(command, commandType), true);
            case TODO, DEADLINE, EVENT -> addTask(parser.parseTask(command, commandType));
            case BYE -> {
                // The main loop handles this command before dispatch.
            }
        }
    }

    /**
     * Deletes the task identified by its one-based task number.
     *
     * @param taskNumber One-based number of the task to delete.
     */
    private void deleteTask(int taskNumber) throws AlfredException, IOException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AlfredException("Invalid task number.");
        }

        Task deletedTask = tasks.delete(taskNumber - 1);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Marks or unmarks the task identified by its one-based task number.
     *
     * @param taskNumber One-based number of the task to update.
     * @param isMarkedAsDone Whether the task should be marked as done.
     */
    private void updateTaskStatus(int taskNumber, boolean isMarkedAsDone)
            throws AlfredException, IOException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AlfredException("Invalid task number.");
        }

        Task task = tasks.get(taskNumber - 1);
        if (isMarkedAsDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        ui.showTaskStatusChanged(isMarkedAsDone);
        storage.save(tasks);
        ui.showTask(task);
    }

    /**
     * Adds a task to the task list and displays the updated task count.
     *
     * @param task Task to add.
     */
    private void addTask(Task task) throws IOException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
