package mybff;

/** Displays tasks with descriptions containing a keyword without changing stored tasks. */
public class FindCommand extends Command {
    private final String keyword;

    /** Creates a search command with a nonempty, case-sensitive keyword. */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** Displays tasks whose descriptions contain the keyword in their original order. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printMatchingTasks(tasks.find(keyword));
    }
}
