package zack;

import zack.command.Command;
import zack.task.TaskList;

import java.io.IOException;

/** Runs the task manager and coordinates commands, user interaction, and storage. */
public class Zack {
    private final Ui ui = new Ui();

    /**
     * Starts the command-line task manager.
     *
     * @param args command-line arguments; currently unused
     */
    public static void main(String[] args) {
        new Zack().run();
    }

    /**
     * Loads tasks and processes commands until exit or end of input.
     * Saves task changes after successful modifying commands.
     * Reports storage errors and stops startup if loading fails.
     */
    public void run() {
        Storage storage = new Storage();
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showMessage(" Cannot load data/zack.properties: " + e.getMessage());
            return;
        }

        printGreeting();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            printHorizontalLine();

            try {
                Command parsedCommand = Parser.parse(command, tasks.size());
                parsedCommand.execute(tasks, ui);
                if (parsedCommand.isExit()) {
                    printHorizontalLine();
                    break;
                }
                if (parsedCommand.changesTasks()) {
                    storage.save(tasks.snapshot());
                }
            } catch (ZackException e) {
                ui.showMessage(" " + e.getMessage());
            } catch (IOException e) {
                ui.showMessage(" Could not save changes to disk: " + e.getMessage());
                ui.showMessage(" Changes remain in memory but have not been saved.");
            }
            printHorizontalLine();
        }
    }

    /** Displays the welcome message between horizontal separators. */
    private void printGreeting() {
        printHorizontalLine();
        ui.showMessage(" Hello! I'm Zack");
        ui.showMessage(" What can I do for you?");
        printHorizontalLine();
    }

    /** Asks the user interface to display a horizontal separator. */
    private void printHorizontalLine() {
        ui.showLine();
    }
}
