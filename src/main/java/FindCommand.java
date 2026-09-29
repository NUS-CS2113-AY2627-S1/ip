/** Shows the tasks whose description contains a keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /** Creates a command that finds tasks whose description contains {@code keyword}. */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.find(keyword));
    }
}
