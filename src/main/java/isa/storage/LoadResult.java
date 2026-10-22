package isa.storage;

import isa.task.TaskList;

import java.util.List;

/**
 * Contains tasks loaded from storage and warnings for skipped records.
 */
public class LoadResult {
    private final TaskList taskList;
    private final List<String> warnings;

    /**
     * Creates a result containing loaded tasks and warnings.
     *
     * @param taskList Tasks loaded successfully.
     * @param warnings Problems found in skipped records.
     */
    public LoadResult(TaskList taskList, List<String> warnings) {
        this.taskList = taskList;
        this.warnings = warnings;
    }

    /**
     * Returns the tasks loaded successfully from storage.
     *
     * @return Loaded task list.
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Returns warnings for records that could not be loaded.
     *
     * @return Warnings about skipped records.
     */
    public List<String> getWarnings() {
        return warnings;
    }
}
