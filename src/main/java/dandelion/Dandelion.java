package dandelion;

import dandelion.command.CommandHandler;
import dandelion.parser.Parser;
import dandelion.storage.Storage;
import dandelion.task.Task;
import dandelion.task.TaskList;
import dandelion.ui.Ui;

import java.io.IOException;

/**
 * Runs the Dandelion chatbot's command loop.
 */
public class Dandelion {
    /**
     * Creates a Dandelion chatbot with its default collaborators.
     */
    public Dandelion() {
    }

    /**
     * Tasks created during the current session.
     */
    private final TaskList tasks = new TaskList();

    /**
     * Loads and saves tasks on the hard disk.
     */
    private final Storage storage = new Storage();

    /**
     * Converts user input into task objects.
     */
    private final Parser parser = new Parser();

    /**
     * Handles console input and session-level messages.
     */
    private final Ui ui = new Ui();

    /** Handles command dispatch and command-specific workflows. */
    private final CommandHandler commandHandler = new CommandHandler(tasks, storage, parser, ui);

    /**
     * Starts the chatbot.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Dandelion().run();
    }

    /**
     * Runs the input loop until the user enters {@code bye}.
     */
    private void run() {
        loadTasks();
        ui.showWelcomeMessage();

        try (Ui currentUi = ui) {
            while (true) {
                String command = currentUi.readCommand();
                if (command == null || !commandHandler.handleCommand(command)) {
                    break;
                }
                ui.showCommandSeparator();
            }
        }
    }

    /**
     * Loads saved tasks into the task list when the chatbot starts.
     */
    private void loadTasks() {
        try {
            ui.showLoading();
            for (Task task : storage.loadTasks()) {
                tasks.add(task);
            }
            if (!tasks.isEmpty()) {
                ui.showLoadedTaskCount(tasks.size());
            } else {
                ui.showNoSaveData();
            }
        } catch (IOException | IllegalArgumentException exception) {
            ui.showLoadingError(exception.getMessage());
        }
    }
}
