package cracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents a command that displays tasks containing a keyword.
 */
public class FindCommand extends Command {
    /** Keyword used to search task descriptions. */
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for the supplied keyword.
     *
     * @param keyword keyword to find
     * @throws CrackerException if the keyword is empty
     */
    public FindCommand(String keyword) throws CrackerException {
        if (keyword.isEmpty()) {
            throw new CrackerException("A find command needs a keyword. Try: find book");
        }
        this.keyword = keyword;
    }

    /**
     * Displays tasks whose descriptions contain this command's keyword, ignoring case.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        ui.showMatchingTasks(matchingTasks);
    }
}
