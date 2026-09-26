package moistbot;

import moistbot.command.Command;
import moistbot.command.Parser;
import moistbot.exception.MoistBotException;
import moistbot.storage.Storage;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Coordinates the storage, task-management, and console-interface components of MoistBot.
 *
 * <p>The application loads saved tasks before accepting commands and keeps processing input
 * until the user exits or the input stream closes.</p>
 */
public final class MoistBot {
    /** The persistence service configured for this application instance. */
    private final Storage storage;

    /** The in-memory tasks owned by this application instance. */
    private final TaskManager taskManager;

    /** The console interface owned by this application instance. */
    private final UserInterface ui;

    /**
     * Creates a MoistBot application that persists tasks at the specified file path.
     *
     * @param filePath the path used to load and save tasks
     */
    public MoistBot(String filePath) {
        storage = new Storage(filePath);
        taskManager = new TaskManager();
        ui = new UserInterface();
    }

    /**
     * Starts MoistBot using its default task data file.
     *
     * @param args command-line arguments, which MoistBot does not use
     */
    public static void main(String[] args) {
        new MoistBot("data/moistbot.txt").run();
    }

    /**
     * Loads saved tasks and runs the interactive command loop.
     *
     * <p>The loop ends when an exit command is processed or the input stream closes. Closing the
     * user interface also releases the input resource at the end of the application lifetime.</p>
     */
    public void run() {
        ui.printWelcome();
        loadSavedTasks();

        boolean isExit = false;
        try (UserInterface activeUi = ui) {
            while (!isExit && activeUi.hasNextCommand()) {
                isExit = processCommand(activeUi.readCommand());
            }
        }
    }

    /**
     * Restores persisted tasks into the task manager.
     *
     * <p>A storage error is shown to the user instead of terminating the application, allowing
     * the command loop to continue with an empty task list.</p>
     */
    private void loadSavedTasks() {
        try {
            taskManager.setTasks(storage.loadTasks());
        } catch (MoistBotException e) {
            ui.printMessage(e.getMessage());
        }
    }

    /**
     * Parses and executes one command while handling errors at the application boundary.
     *
     * <p>Expected command errors and unexpected runtime errors are reported to the user so that a
     * malformed command does not terminate the command loop.</p>
     *
     * @param input the command entered by the user
     * @return {@code true} if the command requests an application exit; {@code false} otherwise
     */
    private boolean processCommand(String input) {
        try {
            Command command = Parser.parseInput(input);
            command.execute(taskManager, ui, storage);
            return command.isExit();
        } catch (MoistBotException e) {
            ui.printMessage(e.getMessage());
            return false;
        } catch (RuntimeException e) {
            ui.printMessage("My apologies, but I could not process that command because of an unexpected "
                    + "internal error. Please check the command format and try again. If the matter persists, "
                    + "please restart MoistBot.");
            return false;
        }
    }

}
