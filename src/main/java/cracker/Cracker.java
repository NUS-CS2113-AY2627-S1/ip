package cracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the Cracker chatbot application.
 */
public class Cracker {
    /** Banner printed when the chatbot starts. */
    private static final String BANNER = " ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗ \n"
            + "██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗\n"
            + "██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝\n"
            + "██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗\n"
            + "╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║\n"
            + " ╚═════╝╚═╝  ╚═╝╚═╝  ╚════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝\n";

    /** Tasks managed during this chatbot session. */
    private final List<Task> tasks;

    /** User interface used to communicate with the user. */
    private final Ui ui;

    /** Storage used to load and save tasks. */
    private final Storage storage;

    /**
     * Creates the chatbot with an empty task list and its collaborators.
     */
    public Cracker() {
        tasks = new ArrayList<>();
        ui = new Ui();
        storage = new Storage();
    }

    /**
     * Starts the Cracker chatbot application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Cracker().run();
    }

    /**
     * Starts the chatbot command loop until the user enters the exit command.
     */
    public void run() {
        ui.showGreeting();
        storage.loadTasks(tasks);
        boolean isExit = false;
        while (!isExit) {
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (CrackerException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

}
