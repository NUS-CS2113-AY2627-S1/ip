package nico;

import nico.command.Command;

/**
 * Entry point of Nico, a command-line chatbot that keeps track of
 * todos, deadlines and events, and saves them between runs.
 */
public class Nico {
    private static final String SAVE_FILE_PATH = "data/nico.txt";

    private final Ui ui;
    private final Storage storage;
    private TaskList taskList;

    /**
     * Creates the chatbot and loads any previously saved tasks.
     *
     * @param filePath Location of the save file.
     */
    public Nico(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage.load());
        } catch (NicoException e) {
            ui.showError(e.getMessage());
            taskList = new TaskList();
        }
    }

    /**
     * Greets the user, then reads and carries out commands until the user types bye.
     */
    public void run() {
        ui.showGreeting();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            ui.showDivider();
            try {
                Command command = Parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (NicoException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Starts the program.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        new Nico(SAVE_FILE_PATH).run();
    }
}