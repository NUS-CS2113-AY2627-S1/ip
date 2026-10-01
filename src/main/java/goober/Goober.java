package goober;

import java.util.Scanner;

import goober.task.Deadline;
import goober.task.Event;
import goober.task.Task;
import goober.task.TaskList;
import goober.task.Todo;

/**
 * Runs the Goober task management application.
 */
public class Goober {

    /**
     * Starts the interactive task management application.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Storage storage = new Storage();
        Parser parser = new Parser();
        Ui ui = new Ui();
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();
        storage.load(taskList);
        CommandHandler commandHandler = new CommandHandler(taskList, ui, parser, storage);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (commandHandler.handleCommand(input)) {
                break;
            }
        }
        scanner.close();
    }
}
