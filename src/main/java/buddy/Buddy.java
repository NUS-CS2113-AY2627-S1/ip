package buddy;

import java.nio.file.Path;

import buddy.command.Command;
import buddy.exception.BuddyException;
import buddy.parser.Parser;
import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    private static final Path SAVE_FILE_PATH = Path.of("data", "buddy.txt");

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Buddy, loading any previously saved tasks from the given save file.
     *
     * @param filePath path to the save file
     */
    public Buddy(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();
        for (Task task : storage.load()) {
            tasks.add(task);
        }
    }

    /**
     * Runs Buddy's main loop: greet the user, then repeatedly read, parse,
     * and execute commands until an exit command is entered or input runs out.
     */
    public void run() {
        ui.showGreeting();
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String input = ui.readCommand();
            ui.showLine();
            try {
                Command command = Parser.parse(input);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

    /**
     * Starts Buddy.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        new Buddy(SAVE_FILE_PATH).run();
    }
}
