package ducky.command;

import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/**
 * Shows every chore whose description contains a given keyword. The matches
 * are numbered from 1 within the search results, not by their rank in the
 * full chore list.
 */
public class FindCommand extends Command {
    /** Text to search for in each chore's description. */
    private final String keyword;

    /**
     * Creates a command that finds chores containing the given keyword.
     *
     * @param keyword Text to search for in each chore's description.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        ChoreList matches = chores.find(keyword);
        if (matches.size() == 0) {
            ui.showMessage("QUACK... no chores match \"" + keyword + "\".");
            return;
        }
        ui.showMessage("Here are the matching chores in your list:");
        ui.showChores(matches);
    }
}
