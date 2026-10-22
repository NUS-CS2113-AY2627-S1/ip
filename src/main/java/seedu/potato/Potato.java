package seedu.potato;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Starts the Potato chatbot application.
 */
public class Potato {
    /**
     * Starts the application and processes commands until the user exits.
     *
     * @param args Command-line arguments, which are not used.
     * @throws IOException If the task save file cannot be read or written.
     */
    public static void main(String[] args) throws IOException {
        String banner = " ____   ___    _____     _     _____   ___\n"
                + "|  _ \\ / _ \\  |_   _|   / \\   |_   _| / _ \\\n"
                + "| |_) | | | |   | |    / _ \\    | |  | | | |\n"
                + "|  __/| |_| |   | |   / ___ \\   | |  | |_| |\n"
                + "|_|    \\___/    |_|  /_/   \\_\\  |_|   \\___/\n";

        Command command = new Command(new Scanner(System.in));
        Storage storage = new Storage(Path.of("data", "potato.txt"));
        TaskList taskList = new TaskList(storage, storage.load());
        CommandParser commandParser = new CommandParser(command, taskList);

        command.showGreeting(banner);
        while (command.hasNextCommand()) {
            String userInput = command.getCommand();
            if (commandParser.execute(userInput)) {
                break;
            }
        }
    }
}
