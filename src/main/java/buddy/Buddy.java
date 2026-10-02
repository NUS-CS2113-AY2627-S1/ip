package buddy;

import java.util.Scanner;

import buddy.command.Command;
import buddy.exception.BuddyException;
import buddy.parser.Parser;
import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Buddy is a simple command-line chatbot that can store short pieces of
 * text ("tasks") entered by the user and list them back on request.
 * Tasks are saved to disk as they change and reloaded on startup.
 */
public class Buddy {
    private final TaskList taskList;
    private final Ui ui;
    private final Storage storage;

    /**
     * Creates a Buddy that loads its tasks from, and saves its tasks to,
     * the given file.
     *
     * @param filePath where Buddy's tasks are saved between runs
     */
    public Buddy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList(storage.load());
    }

    /**
     * Runs Buddy: prints the greeting banner, then reads commands from
     * standard input until the user types "bye".
     */
    public void run() {
        ui.showGreeting();
        Scanner scanner = new Scanner(System.in);
        boolean isExit = false;
        while (!isExit && scanner.hasNextLine()) {
            String fullCommand = scanner.nextLine();
            try {
                Command command = Parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (BuddyException e) {
                ui.showMessage(e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Starts Buddy, saving and loading tasks from "data/buddy.txt".
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new Buddy("data/buddy.txt").run();
    }
}
