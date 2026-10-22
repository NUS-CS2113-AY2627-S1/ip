import java.io.IOException;

import ducky.chore.Chore;
import ducky.chore.ChoreList;
import ducky.command.Command;
import ducky.parser.DuckyException;
import ducky.parser.Parser;
import ducky.storage.Storage;
import ducky.ui.Ui;

/**
 * Entry point for the Ducky task manager.
 * Ducky wires together its Ui, Storage and ChoreList, then runs the command
 * loop: reading a command, parsing it into a {@link Command}, and executing
 * that command, until a "bai" command ends the loop.
 */
public class Ducky {
    /** Handles all interaction with the user. */
    private final Ui ui;

    /** Loads and saves the chore list from and to disk. */
    private final Storage storage;

    /** The chores currently tracked, in memory. */
    private final ChoreList chores;

    /** Sets up Ui and Storage, then loads whatever chores were saved from a previous run. */
    public Ducky() {
        ui = new Ui();
        storage = new Storage();
        chores = loadChores();
    }

    /**
     * Runs the Ducky command loop: read a command, parse it, execute it, and
     * repeat until the parsed command signals it should end the loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);
                command.execute(chores, ui, storage);
                isExit = command.isExit();
            } catch (DuckyException e) {
                ui.showMessage(e.getMessage());
            }
        }
        ui.showGoodbye();
        ui.close();
    }

    /**
     * Loads previously saved chores from disk at startup.
     * If nothing is saved yet, or the save file cannot be read, Ducky starts
     * with an empty chore list instead of failing. If only some lines are
     * unreadable, the rest are loaded and the user is warned about the others.
     *
     * @return A chore list containing whatever was loaded (possibly empty).
     */
    private ChoreList loadChores() {
        ChoreList loaded = new ChoreList();
        try {
            for (Chore chore : storage.load()) {
                loaded.add(chore);
            }
            int skippedLineCount = storage.getSkippedLineCount();
            if (skippedLineCount > 0) {
                ui.showMessage("QUACK?! Skipped " + skippedLineCount + " unreadable line(s) in the save file. "
                        + "They will be removed the next time chores are saved.");
            }
        } catch (IOException e) {
            ui.showMessage("QUACK?! Couldn't read saved chores, starting with an empty list: " + e.getMessage());
        }
        return loaded;
    }

    /**
     * Starts Ducky.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Ducky().run();
    }
}
