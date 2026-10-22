package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Displays tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    /** Keyword used to search task descriptions. */
    private final String keyword;

    /**
     * Creates a keyword search command.
     *
     * @param keyword keyword entered by the user
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Searches task descriptions and displays the matching tasks.
     *
     * @param tasks task list to search
     * @param ui user interface used for the response
     * @param storage storage, which is not modified
     * @throws DrPijonException when the keyword is missing
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        if (keyword.isBlank()) {
            throw new DrPijonException("OI FIND MUST INCLUDE A KEYWORD >:( Try: find book");
        }

        String trimmedKeyword = keyword.trim();
        ui.showMatchingTasks(tasks.find(trimmedKeyword), trimmedKeyword);
    }
}
