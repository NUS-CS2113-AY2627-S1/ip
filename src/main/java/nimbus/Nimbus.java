package nimbus;

import java.util.Scanner;

/**
 * Coordinates the Nimbus command-line task manager
 */

public class Nimbus {
    /**
     * Starts Nimbus and processes commands until user exits.
     *
     * @param args Command-line arguments, not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList tasks;
        try {
            tasks = new TaskList(Storage.loadTasks());
        } catch (IllegalStateException exception) {
            ui.showMessage(exception.getMessage());
            return;
        }
        Parser parser = new Parser();

        ui.showWelcome();


        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (input.equals("bye")) {
                    break;
                }
                parser.execute(input, tasks, ui);
            }
        }
        ui.showGoodbye();
    }
}


