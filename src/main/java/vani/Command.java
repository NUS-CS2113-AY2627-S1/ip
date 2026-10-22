package vani;

import vani.task.Task;

/**
 * Holds the meaning of a user command after parsing, without executing it.
 */
public class Command {
    /**
     * Identifies the operations supported by Vani and their command keywords.
     */
    public enum Type {
        /** Ends the interactive session. */
        BYE("bye"),
        /** Displays every task in the list. */
        LIST("list"),
        /** Searches for text in task descriptions. */
        FIND("find"),
        /** Marks a task as completed. */
        MARK("mark"),
        /** Marks a task as incomplete. */
        UNMARK("unmark"),
        /** Removes a task from the list. */
        DELETE("delete"),
        /** Adds a task containing only a description. */
        TODO("todo"),
        /** Adds a task with a due date. */
        DEADLINE("deadline"),
        /** Adds a task with a start date and an end date. */
        EVENT("event");

        private final String keyword;

        /**
         * Associates an operation with the keyword entered by the user.
         *
         * @param keyword the text that identifies this operation
         */
        Type(String keyword) {
            this.keyword = keyword;
        }

        /**
         * Returns the command keyword, also used to name newly added task types.
         *
         * @return the keyword entered by the user
         */
        public String getKeyword() {
            return keyword;
        }
    }

    private final Type type;
    private final int taskNumber;
    private final Task task;
    private final String searchKeyword;

    /**
     * Creates a parsed command. Only Parser needs to construct these objects.
     *
     * @param type the operation to perform
     * @param taskNumber the task number for mark, unmark, or delete; otherwise 0
     * @param task the new task for an add command; otherwise null
     */
    Command(Type type, int taskNumber, Task task) {
        this.type = type;
        this.taskNumber = taskNumber;
        this.task = task;
        searchKeyword = null;
    }

    /**
     * Creates a find command containing its search keyword.
     *
     * @param searchKeyword the text to search for in task descriptions
     */
    Command(String searchKeyword) {
        type = Type.FIND;
        taskNumber = 0;
        task = null;
        this.searchKeyword = searchKeyword;
    }

    /**
     * Returns the operation represented by this command.
     *
     * @return the command type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the user-facing task number for a mark, unmark, or delete command.
     *
     * @return the task number, or 0 for commands that do not use one
     */
    public int getTaskNumber() {
        return taskNumber;
    }

    /**
     * Returns the new task supplied by a todo, deadline, or event command.
     *
     * @return the task to add, or null for other commands
     */
    public Task getTask() {
        return task;
    }

    /**
     * Returns the search keyword supplied by a find command.
     *
     * @return the search keyword, or null for other commands
     */
    public String getSearchKeyword() {
        return searchKeyword;
    }
}
