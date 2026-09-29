package chillguy;

import java.nio.file.Path;

import chillguy.exception.ChillguyException;
import chillguy.parser.Command;
import chillguy.parser.Parser;
import chillguy.storage.Storage;
import chillguy.task.Task;
import chillguy.task.TaskList;
import chillguy.ui.Ui;

/**
 * Coordinates console interaction, command parsing, and the persistent task list.
 */
public class Chillguy {
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a chatbot using the default task file relative to the working directory.
     */
    public Chillguy() {
        this(Path.of("data", "chillguy.txt"));
    }

    /**
     * Creates a chatbot with a chosen storage location, loading tasks when it runs.
     */
    public Chillguy(Path filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
    }

    /**
     * Starts the chatbot and processes commands until bye or the end of input.
     */
    static void main() {
        new Chillguy().run();
    }

    /**
     * Loads saved tasks and runs the command loop, stopping if loading fails.
     */
    public void run() {
        ui.showGreeting();
        try {
            tasks = new TaskList(storage.load(), storage);
        } catch (ChillguyException exception) {
            ui.showLoadingError(exception.getMessage());
            return;
        }

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            ui.showSeparator();
            try {
                Command command = parser.parse(input);
                if (execute(command)) {
                    break;
                }
            } catch (ChillguyException exception) {
                ui.showError(exception.getMessage());
            }
            ui.showSeparator();
        }
    }

    /**
     * Applies a parsed command and confirms changes only after they have been saved.
     *
     * @return Whether the user requested exit.
     */
    private boolean execute(Command command) throws ChillguyException {
        switch (command.type()) {
            case EXIT -> {
                ui.showExitMessage();
                return true;
            }
            case LIST -> ui.showTasks(tasks.getTasks());
            case FIND -> ui.showMatchingTasks(tasks.find(command.keyword()));
            case ADD -> {
                tasks.add(command.task());
                ui.showAddedTask(command.task(), tasks.size());
            }
            case DELETE -> {
                Task deletedTask = tasks.delete(command.taskNumber());
                ui.showDeletedTask(deletedTask, tasks.size());
            }
            case MARK -> ui.showTaskStatus(tasks.updateStatus(command.taskNumber(), true));
            case UNMARK -> ui.showTaskStatus(tasks.updateStatus(command.taskNumber(), false));
        }
        return false;
    }
}
