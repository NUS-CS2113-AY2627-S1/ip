package drpijon.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.DateTimeParser;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Displays deadlines and events occurring on a specified date.
 */
public class OnCommand extends Command {
    /** Date text supplied with the lookup command. */
    private final String arguments;

    /**
     * Creates a date lookup command.
     *
     * @param arguments date entered by the user
     */
    public OnCommand(String arguments) {
        this.arguments = arguments;
    }

    /**
     * Displays deadlines and events occurring on the requested date.
     *
     * @param tasks task list to search
     * @param ui user interface used for the response
     * @param storage storage, which is not modified
     * @throws DrPijonException when the date is missing or invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        if (arguments.isBlank()) {
            throw new DrPijonException("OI ON MUST INCLUDE A DATE >:( Try: on 2026-08-06");
        }

        LocalDate date;
        try {
            date = DateTimeParser.parseDate(arguments);
        } catch (DateTimeParseException e) {
            throw new DrPijonException("OI ON DATE MUST USE yyyy-MM-dd >:( Try: on 2026-08-06");
        }

        ui.showTasksOnDate(tasks.findOnDate(date), date);
    }
}
