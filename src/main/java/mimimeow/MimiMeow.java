package mimimeow;

import java.nio.file.Path;
import java.util.Scanner;

import mimimeow.command.CommandHandler;
import mimimeow.command.CommandParser;
import mimimeow.exception.MimiMeowException;
import mimimeow.storage.Storage;
import mimimeow.task.TaskList;
import mimimeow.ui.MimiMeowUi;

/** Runs the MimiMeow command-line task manager. */
public class MimiMeow {
    private final MimiMeowUi ui;
    private final CommandHandler commandHandler;
    private final String startupErrorMessage;

    /** Creates MimiMeow with its task storage, parser, command handler, and UI. */
    public MimiMeow() {
        Storage storage = new Storage(Path.of("data", "mimimeow.txt"));
        this.ui = new MimiMeowUi();
        TaskList taskList;
        String startupErrorMessage = null;
        try {
            taskList = storage.load();
        } catch (MimiMeowException exception) {
            taskList = new TaskList();
            startupErrorMessage = exception.getMessage();
        }
        this.startupErrorMessage = startupErrorMessage;
        this.commandHandler = new CommandHandler(taskList, new CommandParser(), ui, storage);
    }

    /**
     * Starts the MimiMeow command-line application.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new MimiMeow().run();
    }

    /** Runs the application input loop until the user enters the bye command. */
    private void run() {
        ui.showWelcomeMessage();
        if (startupErrorMessage != null) {
            ui.showError(startupErrorMessage);
            ui.showSeparator();
        }
        Scanner inputScanner = new Scanner(System.in);
        while (true) {
            String userInput = ui.readUserInput(inputScanner);
            if (commandHandler.execute(userInput)) {
                return;
            }
        }
    }
}
