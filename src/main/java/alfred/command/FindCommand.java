package alfred.command;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;

/**
 * Displays tasks whose descriptions contain specified search text.
 */
public class FindCommand extends Command {

    private final String searchText;

    /**
     * Creates a command that searches task descriptions.
     *
     * @param searchText Text to find in task descriptions.
     */
    public FindCommand(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showFindResults(tasks.findByDescription(searchText));
    }
}
