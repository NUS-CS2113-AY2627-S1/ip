package lumi;

import java.util.ArrayList;
import java.util.List;

import lumi.command.Command;

/**
 * A command-line task tracker. Lumi reads commands from standard input,
 * records todos, deadlines and events, and reports the list back on request.
 */
public class Lumi {
    /** Where the task list lives, relative to the folder Lumi is run from. */
    private static final String SAVE_FILE_PATH = "data/lumi.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Wires up the parts Lumi needs. Nothing is read or printed here, so the
     * order in which the user sees things stays decided by {@link #run()}.
     *
     * @param filePath Where the task list is kept between runs.
     */
    public Lumi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();
    }

    /**
     * Greets the user, restores the saved tasks, then serves one command at a
     * time until the user says bye or the input runs out. A command that fails
     * reports why and the session carries on.
     */
    public void run() {
        ui.showWelcome();
        loadTasks();
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }
            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (LumiException e) {
                ui.show(e.getMessage());
            }
        }
        ui.close();
        ui.show("Bye. Hope to see you again soon!");
    }

    /**
     * Starts Lumi against its usual save file.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        new Lumi(SAVE_FILE_PATH).run();
    }

    /**
     * Fills the task list with whatever a previous run saved. A save file that
     * cannot be read is reported and the session starts with an empty list,
     * so a damaged file never stops Lumi from running.
     */
    private void loadTasks() {
        List<String> skipped = new ArrayList<>();
        try {
            tasks.addAll(storage.load(skipped));
        } catch (LumiException e) {
            ui.show(e.getMessage(), "Starting with an empty list.");
            return;
        }
        if (!skipped.isEmpty()) {
            ArrayList<String> report = new ArrayList<>(skipped);
            report.add("Everything else in the file was loaded.");
            ui.show(report.toArray(new String[0]));
        }
    }
}
