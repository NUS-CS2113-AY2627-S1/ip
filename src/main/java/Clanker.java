import java.io.IOException;
import java.util.Scanner;

import Task.TasksList;
import Ui.Ui;
import Ui.Greeting;
import Ui.Storage;

/** Restores saved tasks and runs the interactive command loop. */
public class Clanker {

    /** Loads the saved task list before accepting new user input. */
    public static void main(String[] args) {
        TasksList tasks;
        Greeting greeting = new Greeting();
        Storage storage = new Storage();

        try {
            tasks = storage.loadTasks();
        } catch (IOException e) {
            System.out.println("Could not load tasks: " + e.getMessage());
            // Stop so an unreadable save cannot be overwritten with an incomplete list.
            return;
        }
        Ui command = new Ui(tasks);

        try (Scanner in = new Scanner(System.in)) {
            // Start Up
            greeting.greetUser();
            command.processCommand("list");

            // Process Commands
            while (in.hasNextLine()) {
                String userInput = in.nextLine();

                if (userInput.isBlank()) {
                    continue;
                }

                if (command.processCommand(userInput)) {
                    break;
                }
            }
        }
    }
}
