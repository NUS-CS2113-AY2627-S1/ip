package mybff;

import java.io.IOException;
import java.nio.file.Path;

/** Coordinates console interaction, command parsing, storage, and the task list. */
public class MyBff {
    private final Ui ui = new Ui();
    private final Parser parser = new Parser();
    private final Storage storage = new Storage(Path.of("data", "mybff.txt"));
    private TaskList tasks;

    /**
     * Starts the chatbot.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new MyBff().run();
    }

    /** Greets the user and handles commands until bye or the end of input. */
    public void run() {
        try {
            tasks = storage.load();
        } catch (IOException | SecurityException exception) {
            ui.printLoadError();
            ui.close();
            return;
        }

        ui.printGreeting();
        try {
            while (ui.hasNextCommand()) {
                String fullCommand = ui.readCommand();
                ui.printSeparator();
                try {
                    Command command = parser.parse(fullCommand);
                    command.execute(tasks, ui, storage);
                    if (command.isExit()) {
                        break;
                    }
                } catch (NumberFormatException exception) {
                    ui.printInvalidTaskNumber();
                } catch (MyBffException exception) {
                    ui.printError(exception);
                }
                ui.printResponseEnd();
            }
        } finally {
            ui.close();
        }
    }
}
