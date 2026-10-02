package fella;

import task.Task;
import storage.Storage;
import parser.Parser;
import tasklist.TaskList;
import constants.Constants;

/** Runs the task manager and delegates commands to its parser, task list, and storage.
 * @param <C> constants type used by the selected personality
 */
public abstract class AbstractFella<C extends Constants> {
    // ============================================== VARIABLES ============================================================
    /**
     * Constants
     */
    final String SAVE_FILE_PATH = "./data/SmartFella.txt";

    /**
     * Tracking variables
     */
    static boolean isRunning;
    /** Constants used for commands and user-facing messages. */
    protected C c;
    private TaskList tasks;
    private Storage storage;
    private Parser parser;

    /** Creates a fella using the supplied personality-specific constants.
     * @param constants messages and keywords used by this fella
     */
    protected AbstractFella(C constants) {
        this.c = constants;
        this.tasks = new TaskList(constants);
        this.storage = new Storage(SAVE_FILE_PATH, constants);
        this.parser = new Parser(constants);
        isRunning = true;
    }

    // ============================================== PRINT MESSAGES ============================================================
    /** Prints the personality artwork; subclasses may override the output style. */
    public void printFella() {
        System.out.println(c.FELLA_STRING);
    }

    /** Prints the greeting configured in the constants.
     * Called by {@link #run()}.
     */
    public void printGreeting() {
        System.out.println(c.GREETING_STRING);
    }

    /** Prints the goodbye message configured in the constants.
     * Called by {@link #run()}.
     */
    public void printGoodbye() {
        System.out.println(c.GOODBYE_STRING);
    }

    /**
     * Matches one user command and performs its task-list or storage action.
     * @param input complete command entered by the user
     */
    public void matchInput(String input) {
        if (input.equals(c.BYE_KEYWORD)) {
            isRunning = false;

        } else if (input.equals(c.LIST_KEYWORD)
                || input.startsWith(c.LIST_KEYWORD + " ")){
            tasks.getList(input);

        } else if (input.startsWith(c.FIND_KEYWORD + " ")) {
            tasks.find(input);

        } else if (input.startsWith(c.MARK_KEYWORD)
                || input.startsWith(c.UNMARK_KEYWORD)) {
            tasks.markDone(input);
            storage.updateData(tasks);

        } else if (input.startsWith(c.TODO_KEYWORD)){
            Task new_task = tasks.addTodo(input);
            if (new_task != null) {
                storage.saveData(new_task);
            }

        } else if (input.startsWith(c.DEADLINE_KEYWORD)){
            Task new_task = tasks.addDeadline(input);
            if (new_task != null) {
                storage.saveData(new_task);
            }

        } else if (input.startsWith(c.EVENT_KEYWORD)){
            Task new_task = tasks.addEvent(input);
            if (new_task != null) {
                storage.saveData(new_task);
            }

        } else if (input.startsWith(c.DELETE_KEYWORD)) {
            boolean deleteSuccess = tasks.delete(input);
            if (deleteSuccess) {
                storage.updateData(tasks);
            }

        } else {
            System.out.println(c.INCORRECT_COMMAND_STRING);
        }
    }

    // ============================================== MAIN FUNCTION ============================================================
    /** Starts the application, loads saved tasks, and repeatedly reads commands. */
    public void run() {
        printFella();
        printGreeting();

        //load current data if exists
        tasks = storage.loadData(tasks);

        // main process
        String input;
        while (isRunning) {
            input = parser.getInput();
            matchInput(input);
        }

        printGoodbye();
    }
}
