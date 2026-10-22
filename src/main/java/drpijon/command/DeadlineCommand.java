package drpijon.command;

import java.time.format.DateTimeParseException;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Deadline;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Adds a deadline task.
 */
public class DeadlineCommand extends AddCommand {
    /**
     * Creates a deadline command.
     *
     * @param description deadline description and due date
     */
    public DeadlineCommand(String description) {
        super(description);
    }

    /**
     * Creates, saves, and displays a deadline task.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the new task
     * @throws DrPijonException when the deadline format is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        String[] deadlineParts = getDescription().split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new DrPijonException("OI DEADLINE MUST INCLUDE /by >:( Try: deadline return book /by "
                    + "2019-10-15 1800");
        }

        Deadline deadline;
        try {
            deadline = Deadline.fromText(deadlineParts[0].trim(), deadlineParts[1].trim());
        } catch (DateTimeParseException e) {
            throw new DrPijonException("OI DEADLINE DATE/TIME MUST USE yyyy-MM-dd or yyyy-MM-dd HHmm >:( Try: "
                    + "deadline return book /by 2019-10-15 1800");
        }

        addTask(deadline, tasks, ui, storage);
    }
}
