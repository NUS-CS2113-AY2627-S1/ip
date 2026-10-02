package Ui;

/** Supplies the shared command guide shown at startup and after input errors. */
public final class HelpText {
    /** Command syntax displayed at startup, for help, and after input errors. */
    public static final String COMMAND_LIST = """
            \n
            How to use CLANKER:
            todo 'command' (add todo task)
            deadline 'command' /by 'due date' (add deadline task with deadline)
            event 'command' /from 'start date' /to 'end date' (add event task with from and to date)
            list (list all tasks added)
            find 'keyword' (find tasks whose descriptions contain the keyword; case-sensitive)
            mark 'N' (mark task N as done)
            unmark 'N' (mark task N as not done)
            delete 'N' (delete task N from list)
            """;
    /** Prevents construction because this class only holds shared text. */
    private HelpText() {}
}
