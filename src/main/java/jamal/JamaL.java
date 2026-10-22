package jamal;

import jamal.command.Command;

/**
 * Entry point of the JamaL chatbot, a deadpan command-line task manager.
 * Wires together the Ui, Storage, TaskList and Parser, then runs the main loop.
 */
public class JamaL {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /** Creates the chatbot with an empty task list and the default save file. */
    public JamaL() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList();
    }

    /**
     * Fills the task list from the save file. Problems are reported to the
     * user but never stop the chatbot from starting; it just starts with
     * fewer (or no) tasks.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
            if (storage.getCorruptLineCount() > 0) {
                ui.showError("skipped " + storage.getCorruptLineCount()
                        + " broken line(s) in the save file.");
            }
        } catch (JamaLException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Greets the user, loads saved tasks, then reads and runs commands
     * until the user types "bye".
     */
    public void run() {
        ui.showWelcome();
        loadTasks();
        boolean isExit = false;
        while (!isExit) {
            // Every user-facing error is thrown as a JamaLException and caught
            // here, so all error messages are printed from one place.
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (JamaLException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
    }

    /**
     * Starts the chatbot.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new JamaL().run();
    }
}
