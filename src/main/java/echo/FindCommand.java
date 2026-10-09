package echo;

/**
 * Shows the tasks whose description contains a given keyword.
 */
public class FindCommand extends Command {
    /** The text to look for in task descriptions. */
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for a keyword.
     *
     * @param keyword the text to look for; matching is case-sensitive.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** Shows the tasks whose description contains the keyword. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.find(keyword));
    }
}
