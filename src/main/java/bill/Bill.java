package bill;

import bill.command.Command;
import bill.exception.BillException;
import bill.parser.Parser;
import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

import java.util.List;

/**
 * Coordinates Bill's parser, task list, storage, and user interface.
 */
public class Bill {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a Bill application using the supplied input/output and data file services.
     *
     * @param ui Console user interface.
     * @param storage Task persistence service.
     */
    public Bill(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
    }

    /**
     * Loads saved tasks and handles commands until the user enters bye or input ends.
     */
    public void run() {
        tasks = loadTasks();
        ui.showWelcome();
        while (ui.hasNextCommand()) {
            try {
                Command command = Parser.parse(ui.readCommand());
                if (command.isExit()) {
                    break;
                }
                command.execute(tasks, ui, storage);
            } catch (BillException exception) {
                ui.showError(exception.getMessage());
            }
            ui.showLine();
        }
        ui.showGoodbye();
    }

    /**
     * Falls back to an empty list if saved tasks cannot be read this session.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (BillException exception) {
            ui.showError(exception.getMessage());
            ui.showLoadingFallback();
            return new TaskList(List.of());
        }
    }

    /**
     * Starts the console application with Bill's default task file.
     *
     * @param args Command-line arguments; not used by this application.
     */
    public static void main(String[] args) {
        try (Ui ui = new Ui()) {
            new Bill(ui, new Storage()).run();
        }
    }
}
