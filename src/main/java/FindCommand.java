/**
 * Shows the user every task whose description mentions a keyword.
 * <p>
 * Searching changes nothing, so unlike the commands that alter the list this
 * one does not save afterwards.
 */
public class FindCommand extends Command {

    /** What to look for in task descriptions. */
    private final String keyword;

    /**
     * Constructs a command that searches for the given keyword.
     *
     * @param keyword word or phrase to look for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.find(keyword), keyword);
    }
}
