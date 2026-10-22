package bob;

import bob.command.Command;
import bob.exception.EmptyError;
import bob.exception.IndexOutOfBoundsError;
import bob.exception.InvalidTaskNumberError;
import bob.exception.SyntaxError;
import bob.exception.TaskNotFoundError;
import bob.parser.Parser;
import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Runs the B.O.B. command-line task manager.
 */
public class Bob {
    private static Ui ui;
    private static TaskList tasks;
    private static Storage storage;

    /**
     * Starts the B.O.B. command-line application.
     */
    public static void main(String[] args) {
        ui = new Ui();
        storage = new Storage("./data/bob.txt");
        ui.showWelcome();
        tasks = storage.loadTasks(ui);
        runCommandLoop();
    }

    /**
     * Reads and executes commands until exit or end of input, reporting recoverable errors.
     */
    private static void runCommandLoop() {
        try (Ui console = ui) {
            while (console.hasNextCommand()) {
                String input = console.readCommand();

                try {
                    Command command = Parser.parse(input);
                    command.execute(tasks, ui, storage);
                    if (command.isExit()) {
                        break;
                    }
                } catch (EmptyError | SyntaxError | IndexOutOfBoundsError | InvalidTaskNumberError
                        | TaskNotFoundError e) {
                    ui.showError(e.getMessage());
                }
            }
        }
    }
}
