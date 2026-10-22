package dawn.command;

import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Displays the same command guide shown when Dawn starts.
 */
public class HelpCommand extends Command {
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        ui.showCommandGuide();
    }
}
