package turing;

import turing.command.Command;
import turing.task.TaskList;

/**
 * Entry point of the Turing chatbot.
 * Supports greeting the user, adding todos, deadlines and events, listing the
 * stored tasks, marking them as done or not done, and exiting when the user
 * types "bye".
 */
public class Turing {
    /** Where the tasks are kept between runs, relative to where the chatbot is started. */
    private static final String SAVE_FILE_PATH = "data/turing.txt";

    /** Talks to the user: reads their commands and shows them every reply. */
    private final Ui ui;

    /** Tasks entered so far. */
    private final TaskList tasks;

    /** Reads and writes the save file holding those tasks. */
    private final Storage storage;

    /**
     * Creates a chatbot that remembers its tasks in the given file.
     *
     * @param saveFilePath Path to the save file, relative to where the chatbot is run.
     */
    public Turing(String saveFilePath) {
        ui = new Ui();
        tasks = new TaskList();
        storage = new Storage(saveFilePath);
    }

    /**
     * Carries out one line of user input and reports whether the chatbot should stop.
     * Anything the user can put right is reported back to them and the
     * conversation carries on, so a mistyped command never ends the session.
     *
     * @param rawInput One line of input, exactly as the user typed it.
     * @return True if the user asked to exit.
     */
    private boolean handleInput(String rawInput) {
        try {
            Command command = Parser.parse(rawInput);
            command.execute(tasks, ui, storage);
            return command.isExit();
        } catch (TuringException exception) {
            ui.showError(exception);
            return false;
        }
    }

    /**
     * Fills the task list from the save file, explaining anything that went
     * wrong. A problem here is not fatal: the chatbot carries on with whatever
     * it managed to read, which on a first run is nothing at all.
     */
    private void loadTasks() {
        try {
            int skippedLineCount = storage.load(tasks);
            if (skippedLineCount > 0) {
                ui.showSkippedSaveLines(skippedLineCount);
            } else if (!tasks.isEmpty()) {
                ui.showTasksRestored(tasks.getTaskCount());
            }
        } catch (TuringException exception) {
            ui.showError(exception);
        }
    }

    /**
     * Runs the chatbot, reading commands from standard input until the user
     * says goodbye or the input ends.
     */
    public void run() {
        ui.showWelcome();
        loadTasks();

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            isExit = handleInput(ui.readCommand());
        }
    }

    /**
     * Starts one chatbot session.
     *
     * @param args Command line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Turing(SAVE_FILE_PATH).run();
    }
}
