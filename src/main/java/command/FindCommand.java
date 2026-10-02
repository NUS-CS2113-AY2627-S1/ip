package command;

import display.Ui;
import exception.AtlasException;
import list.TaskList;
import storage.Storage;
import task.Task;

import java.util.ArrayList;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        if (keyword.isEmpty()) {
            throw new AtlasException("Invalid FIND: Please enter a keyword to search for");
        }
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.displayMatchingTasks(matchingTasks);
    }
}