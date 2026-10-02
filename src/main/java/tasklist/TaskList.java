package tasklist;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import error.EmptyArgumentException;
import error.IncorrectArgumentCountException;
import error.IncorrectArgumentFormatException;
import error.InvalidRangeException;
import error.TaskAlreadyMarkedException;
import error.TaskAlreadyUnmarkedException;
import error.TooFewArgumentsException;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import constants.Constants;

/** Stores tasks and handles task-related commands from {@code AbstractFella}. */
public class TaskList {
    /** Recognises the accepted user date formats. */
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(\\d{2})-(\\d{2})(?:-(\\d{2}|\\d{4}))?(?:\\s+(\\d{4}))?$");

    /** Tasks in their current insertion order. */
    ArrayList<Task> tasks;
    /** Number of tasks currently stored. */
    int taskListSize;
    /** Keywords and messages used when commands are processed. */
    private final Constants c;

    /** Creates an empty list using the supplied constants.
     * @param constants keywords and messages used by this list
     */
    public TaskList(Constants constants) {
        this.tasks = new ArrayList<>();
        this.taskListSize = 0;
        this.c = constants;
    }

    // ============================================== SIZE ============================================================
    /** Returns the number of stored tasks; used by the fella and storage.
     * @return current task count
     */
    public int getSize() {
        return taskListSize;
    }

    /** Returns a task by its zero-based internal index.
     * @param N zero-based task index
     * @return task at the requested index
     */
    public Task getTask(int N) {
        // TODO: add error handling checks
        return tasks.get(N);
    }

    /**
     * Prints every task whose description contains the supplied search term.
     * Search is case-insensitive and preserves the task list order.
     *
     * @param input complete find command, such as {@code find book}
     */
    public void find(String input) {
        String searchTerm = input.substring(c.FIND_KEYWORD.length()).strip();
        if (searchTerm.isEmpty()) {
            System.out.println(c.FIND_EMPTY_ERROR_STRING);
            return;
        }

        String searchTermLowerCase = searchTerm.toLowerCase(Locale.ROOT);
        boolean foundMatch = false;
        for (int i = 0; i < taskListSize; i++) {
            Task task = tasks.get(i);
            if (task.getName().toLowerCase(Locale.ROOT).contains(searchTermLowerCase)) {
                System.out.println(String.format("%d. %s", i + 1, task));
                foundMatch = true;
            }
        }

        if (!foundMatch) {
            System.out.println(c.FIND_NO_MATCHING_TASKS_ERROR_STRING);
            return;
        }
        System.out.println("");
    }

    /**
     * Prints all tasks in their current order; used by the plain {@code list} command.
     */
    public void getList() {
        getList(c.LIST_KEYWORD);
    }

    /**
     * Prints tasks according to optional sorting and filtering requests.
     *
     * @param input complete list command entered by the user
     */
    public void getList(String input) {
        if (taskListSize == 0) {
            System.out.println(c.LIST_EMPTY_ERROR_STRING);
            return;
        }

        String[] arguments = input.strip().split("\\s+");
        if (!isValidListOptions(arguments)) {
            System.out.println(c.LIST_FORMAT_ERROR_STRING);
            return;
        }

        List<Integer> taskIndicesToPrint = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            taskIndicesToPrint.add(i);
        }
        String sortField = null;
        String sortDirection = null;
        List<String> filters = new ArrayList<>();
        for (int i = 1; i < arguments.length;) {
            if (arguments[i].equals(c.LIST_SORT_DELIM)) {
                sortField = arguments[i + 1];
                sortDirection = arguments[i + 2];
                i += 3;
            } else {
                i += 1;
                while (i < arguments.length && !arguments[i].equals(c.LIST_SORT_DELIM)
                        && !arguments[i].equals(c.LIST_FILTER_DELIM)) {
                    filters.add(arguments[i]);
                    i += 1;
                }
            }
        }

        if (!filters.isEmpty()) {
            taskIndicesToPrint.removeIf(index -> !matchesFilters(tasks.get(index), filters));
            if (taskIndicesToPrint.isEmpty()) {
                System.out.println(c.LIST_NO_MATCHING_TASKS_ERROR_STRING);
                return;
            }
        }

        if (sortField != null) {
            Comparator<Task> comparator = getListComparator(sortField, sortDirection);
            taskIndicesToPrint.sort((firstIndex, secondIndex) ->
                    comparator.compare(tasks.get(firstIndex), tasks.get(secondIndex)));
        }

        for (int index : taskIndicesToPrint) {
            System.out.println(String.format("%d. %s",
                index + 1,
                tasks.get(index).toString()));
        }
        System.out.println("");
    }

    /** Validates sort and filter tokens before {@link #getList(String)} prints tasks.
     * @param arguments list command split into tokens
     * @return true when every option is supported
     */
    private boolean isValidListOptions(String[] arguments) {
        if (arguments.length == 1) {
            return true;
        }

        boolean hasSort = false;
        boolean hasFilter = false;
        for (int i = 1; i < arguments.length;) {
            if (arguments[i].equals(c.LIST_SORT_DELIM)) {
                if (hasSort || i + 2 >= arguments.length) {
                    return false;
                }
                String field = arguments[i + 1];
                String direction = arguments[i + 2];
                boolean validField = field.equals(c.LIST_NAME_FIELD)
                        || field.equals(c.LIST_DATE_FIELD);
                boolean validDirection = direction.equals(c.LIST_ASCENDING)
                        || direction.equals(c.LIST_DESCENDING);
                if (!validField || !validDirection) {
                    return false;
                }
                hasSort = true;
                i += 3;
            } else if (arguments[i].equals(c.LIST_FILTER_DELIM)) {
                if (hasFilter || i + 1 >= arguments.length) {
                    return false;
                }
                hasFilter = true;
                boolean hasFilterValue = false;
                i += 1;
                while (i < arguments.length && !arguments[i].equals(c.LIST_SORT_DELIM)
                        && !arguments[i].equals(c.LIST_FILTER_DELIM)) {
                    if (!isValidListFilter(arguments[i])) {
                        return false;
                    }
                    hasFilterValue = true;
                    i += 1;
                }
                if (!hasFilterValue) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /** Checks whether one filter token names a supported task type or status.
     * @param filter filter token from a list command
     * @return true when the token is supported
     */
    private boolean isValidListFilter(String filter) {
        return filter.equals(c.TODO_KEYWORD) || filter.equals(c.DEADLINE_KEYWORD)
                || filter.equals(c.EVENT_KEYWORD) || filter.equals(c.LIST_DONE_FILTER)
                || filter.equals(c.LIST_NOT_DONE_FILTER);
    }

    /** Checks whether a task satisfies the selected type and status filters.
     * @param task task to test
     * @param filters filter tokens from the list command
     * @return true when the task matches every filter category
     */
    private boolean matchesFilters(Task task, List<String> filters) {
        boolean hasTypeFilter = false;
        boolean matchesType = false;
        boolean hasStatusFilter = false;
        boolean matchesStatus = false;

        for (String filter : filters) {
            if (filter.equals(c.TODO_KEYWORD)) {
                hasTypeFilter = true;
                matchesType |= task.getIdentifier() == 'T';
            } else if (filter.equals(c.DEADLINE_KEYWORD)) {
                hasTypeFilter = true;
                matchesType |= task instanceof Deadline;
            } else if (filter.equals(c.EVENT_KEYWORD)) {
                hasTypeFilter = true;
                matchesType |= task instanceof Event;
            } else if (filter.equals(c.LIST_DONE_FILTER)) {
                hasStatusFilter = true;
                matchesStatus |= task.isDone();
            } else if (filter.equals(c.LIST_NOT_DONE_FILTER)) {
                hasStatusFilter = true;
                matchesStatus |= !task.isDone();
            }
        }

        return (!hasTypeFilter || matchesType) && (!hasStatusFilter || matchesStatus);
    }

    /** Builds the comparator requested by a list sort option.
     * @param field name or date sort field
     * @param direction ascending or descending order
     * @return comparator used by {@link #getList(String)}
     */
    private Comparator<Task> getListComparator(String field, String direction) {
        Comparator<Task> comparator;
        if (field.equals(c.LIST_NAME_FIELD)) {
            comparator = Comparator.comparing(Task::getName, String.CASE_INSENSITIVE_ORDER);
        } else {
            comparator = (first, second) -> {
                LocalDateTime firstDate = getTaskDate(first);
                LocalDateTime secondDate = getTaskDate(second);
                if (firstDate == null && secondDate == null) {
                    return 0;
                }
                if (firstDate == null) {
                    return 1;
                }
                if (secondDate == null) {
                    return -1;
                }
                return direction.equals(c.LIST_DESCENDING)
                        ? secondDate.compareTo(firstDate)
                        : firstDate.compareTo(secondDate);
            };
            return comparator;
        }

        return direction.equals(c.LIST_DESCENDING) ? comparator.reversed() : comparator;
    }

    /** Returns the date used when sorting a dated task.
     * @param task task to inspect
     * @return deadline or event start date, or null for a todo
     */
    private LocalDateTime getTaskDate(Task task) {
        if (task instanceof Deadline deadline) {
            return deadline.getDeadline();
        }
        if (task instanceof Event event) {
            return event.getFrom();
        }
        return null;
    }

    // ============================================== ADD/VALIDATE ITEMS ============================================================
    /** Adds an existing task, usually while loading saved data.
     * @param newTask task to append to this list
     */
    public void addTaskObject(Task newTask) {
        tasks.add(newTask);
        taskListSize++;
    }
    /** Adds a todo when the input has a valid description.
     * @param input complete todo command entered by the user
     * @return created todo, or {@code null} when validation fails
     */
    public Task addTodo(String input) {
        String description;

        try {
            isValidTodo(input);
        } catch (IncorrectArgumentFormatException e) {
            System.out.println(c.TODO_FORMAT_ERROR_STRING);
            return null;
        } catch (EmptyArgumentException e) {
            System.out.println(c.TODO_EMPTY_ERROR_STRING);
            return null;
        }

        //add todo
        description = input.substring(c.TODO_KEYWORD.length())
                            .strip();

        Task tempTask =  new Todo(description);
        tasks.add(tempTask);
        taskListSize++;

        printSuccessMessage();

        return tempTask;
    }

    /**
     * Validates the format of a todo command.
     *
     * @param input complete todo command
     * @throws IncorrectArgumentFormatException if no space follows the command keyword
     * @throws EmptyArgumentException if the description is missing
     */
    private void isValidTodo(String input)
            throws IncorrectArgumentFormatException, EmptyArgumentException {
        //check that TODO_KEYWORD is proceeded by a space
        //and that there exists content after the space
        boolean hasLength = (input.length() > c.TODO_KEYWORD.length()+1);
        if (!hasLength) {
            throw new EmptyArgumentException();
        }

        boolean hasSpace = (input.charAt(c.TODO_KEYWORD.length())==' ');
        if (!hasSpace) {
            throw new IncorrectArgumentFormatException();
        }

        if (input.substring(c.TODO_KEYWORD.length()).strip().isEmpty()) {
            throw new EmptyArgumentException();
        }
    }

    /** Adds a deadline when its description and date are valid.
     * @param input complete deadline command entered by the user
     * @return created deadline, or {@code null} when validation fails
     */
    public Task addDeadline(String input) {
        String[] description;
        String text;
        LocalDateTime deadline;

        try {
            isValidDeadline(input);
        } catch (IncorrectArgumentFormatException e) {
            System.out.println(c.DEADLINE_FORMAT_ERROR_STRING);
            return null;
        } catch (IncorrectArgumentCountException e) {
            System.out.println(c.DEADLINE_COUNT_ERROR_STRING);
            return null;
        } catch (EmptyArgumentException e) {
            System.out.println(c.DEADLINE_EMPTY_ERROR_STRING);
            return null;
        }

        //add
        description = input.substring(c.DEADLINE_KEYWORD.length())
                            .strip()
                            .split(c.DEADLINE_DELIM, -1);
        text = description[0].strip();
        try {
            deadline = parseDateTime(description[1].strip());
        } catch (IncorrectArgumentFormatException e) {
            System.out.println(c.DATE_FORMAT_ERROR_STRING);
            return null;
        } catch (DateTimeException e) {
            System.out.println(c.DATE_OUT_OF_RANGE_ERROR_STRING);
            return null;
        }

        Task tempTask =  new Deadline(text, deadline);
        tasks.add(tempTask);
        taskListSize++;

        printSuccessMessage();

        return tempTask;
    }

    /**
     * Validates the format of a deadline command.
     *
     * @param input complete deadline command
     * @throws IncorrectArgumentFormatException if no space follows the command keyword
     * @throws IncorrectArgumentCountException if there is not exactly one {@code /by} delimiter
     * @throws EmptyArgumentException if the description or deadline is empty
     */
    private void isValidDeadline(String input) throws IncorrectArgumentFormatException,
            IncorrectArgumentCountException, EmptyArgumentException {
        //check that DEADLINE_KEYWORD is proceeded by a space
        //and that there exists content after the space
        boolean hasLength = (input.length() > c.DEADLINE_KEYWORD.length()+1);
        if (!hasLength) {
            throw new EmptyArgumentException();
        }

        boolean hasSpace = (input.charAt(c.DEADLINE_KEYWORD.length())==' ');
        if (!hasSpace) {
            throw new IncorrectArgumentFormatException();
        }

        //check that DEADLINE_DELIM exists and that after splitting all substrings are non-empty
        String[] description;

        description = input.substring(c.DEADLINE_KEYWORD.length())
                            .strip()
                            .split(c.DEADLINE_DELIM, -1);

        if (description.length != 2) {
            throw new IncorrectArgumentCountException();
        }

        if (description[0].strip().isEmpty()
            || description[1].strip().isEmpty()) {
            throw new EmptyArgumentException();
        }

    }

    /** Adds an event when its description, dates, and date order are valid.
     * @param input complete event command entered by the user
     * @return created event, or {@code null} when validation fails
     */
    public Task addEvent(String input) {
        String[] description;
        String text;
        LocalDateTime from, to;

        try {
            isValidEvent(input);
        } catch (IncorrectArgumentFormatException e) {
            System.out.println(c.EVENT_FORMAT_ERROR_STRING);
            // TODO: for all of these, maybe do throw error instead, abstractfella will catch the error
            return null;
        } catch (IncorrectArgumentCountException e) {
            System.out.println(c.EVENT_COUNT_ERROR_STRING);
            return null;
        } catch (EmptyArgumentException e) {
            System.out.println(c.EVENT_EMPTY_ERROR_STRING);
            return null;
        }

        //add
        description = input.substring(c.EVENT_KEYWORD.length())
                            .strip()
                            .split(c.EVENT_START_DELIM + "|" + c.EVENT_END_DELIM, -1);

        text = description[0].strip();
        try {
            from = parseDateTime(description[1].strip());
            to = parseDateTime(description[2].strip());
        } catch (IncorrectArgumentFormatException e) {
            System.out.println(c.DATE_FORMAT_ERROR_STRING);
            return null;
        } catch (DateTimeException e) {
            System.out.println(c.DATE_OUT_OF_RANGE_ERROR_STRING);
            return null;
        }

        if (!from.isBefore(to)) {
            System.out.println(c.EVENT_ORDER_ERROR_STRING);
            return null;
        }

        
        Task tempTask =  new Event(text, from, to);
        tasks.add(tempTask);
        taskListSize++;

        printSuccessMessage();

        return tempTask;
    }

    /**
     * Validates the format of an event command.
     *
     * @param input complete event command
     * @throws IncorrectArgumentFormatException if no space follows the command keyword
     * @throws IncorrectArgumentCountException if there are not exactly two time delimiters
     * @throws EmptyArgumentException if the description, start, or end time is empty
     */
    private void isValidEvent(String input) throws IncorrectArgumentFormatException,
            IncorrectArgumentCountException, EmptyArgumentException {
        //check that EVENT_KEYWORD is proceeded by a space
        //and that there exists content after the space
        boolean hasLength = (input.length() > c.EVENT_KEYWORD.length()+1);
        if (!hasLength) {
            throw new EmptyArgumentException();
        }

        boolean hasSpace = (input.charAt(c.EVENT_KEYWORD.length())==' ');
        if (!hasSpace) {
            throw new IncorrectArgumentFormatException();
        }

        String[] description;
        description = input.substring(c.EVENT_KEYWORD.length())
                            .strip()
                            .split(c.EVENT_START_DELIM + "|" + c.EVENT_END_DELIM, -1);

        if (description.length != 3) {
            throw new IncorrectArgumentCountException();
        }

        if (description[0].strip().isEmpty()
            || description[1].strip().isEmpty()
            || description[2].strip().isEmpty()) {
            throw new EmptyArgumentException();
        }

    }

    /**
     * Parses a date in {@code DD-MM-YY HHMM} format. The year defaults to the
     * current year and the time defaults to midnight when omitted.
     *
     * @param input date supplied by the user
     * @return the parsed date and time
     * @throws IncorrectArgumentFormatException if the date is malformed or invalid
     */
    private LocalDateTime parseDateTime(String input) throws IncorrectArgumentFormatException {
        Matcher matcher = DATE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IncorrectArgumentFormatException();
        }

        int day = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        String yearInput = matcher.group(3);
        int year = yearInput == null
                ? LocalDate.now().getYear()
                : yearInput.length() == 2
                    ? 2000 + Integer.parseInt(yearInput)
                    : Integer.parseInt(yearInput);
        int time = matcher.group(4) == null ? 0 : Integer.parseInt(matcher.group(4));
        int hour = time / 100;
        int minute = time % 100;

        return LocalDateTime.of(YearMonth.of(year, month).atDay(day),
                java.time.LocalTime.of(hour, minute));
    }

    /** Marks or unmarks the indexed task according to the command.
     * @param cmd mark or unmark command containing a one-based task number
     */
    public void markDone(String cmd) {
        try {
            isValidMarkDone(cmd);
        } catch (TooFewArgumentsException e) {
            System.out.println(c.MISSING_TASK_NUMBER_STRING);
            return;
        } catch (NumberFormatException e) {
            System.out.println(c.INVALID_NUMBER_STRING);
            return;
        } catch (InvalidRangeException e) {
            System.out.println(c.INVALID_VALUE_STRING);
            return;
        } catch (TaskAlreadyMarkedException e) {
            System.out.println(c.ALREADY_MARKED_STRING);
            return;
        } catch (TaskAlreadyUnmarkedException e) {
            System.out.println(c.ALREADY_UNMARKED_STRING);
            return;
        }

        String[] data = cmd.split(" ");
        int index = Integer.parseInt(data[1]) - 1;
        
        //match with keyword & make change
        if (data[0].startsWith(c.MARK_KEYWORD)) {
            System.out.println(c.MARKED_SUCCESS_STRING
                + Integer.toString(index + 1)
                + "! ! !\n");
            tasks.get(index).markDone();
        } else {
            System.out.println(c.UNMARKED_SUCCESS_STRING
                + Integer.toString(index + 1)
                + "! ! !\n");
            tasks.get(index).unmarkDone();
        }
    }

    /**
     * Validates that a mark or unmark command contains an existing task number.
     *
     * @param input mark or unmark command to validate
     * @throws TooFewArgumentsException if the task number is missing
     * @throws NumberFormatException if the task number is not an integer
     * @throws InvalidRangeException if the task number is outside the task list
     * @throws TaskAlreadyMarkedException if an already marked task is marked again
     * @throws TaskAlreadyUnmarkedException if an already unmarked task is unmarked again
     */
    private void isValidMarkDone(String input)
            throws TooFewArgumentsException, InvalidRangeException, TaskAlreadyMarkedException,
            TaskAlreadyUnmarkedException {
        String[] data = input.split(" ");

        if (data.length < 2) {
            throw new TooFewArgumentsException();
        }

        try {
            Integer.parseInt(data[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }

        // check if within range
        int index = Integer.parseInt(data[1]) - 1;
        if (index >= taskListSize
                || index < 0) {
            throw new InvalidRangeException();
        }

        if (data[0].startsWith(c.MARK_KEYWORD) && tasks.get(index).isDone()) {
            throw new TaskAlreadyMarkedException();
        }

        if (data[0].startsWith(c.UNMARK_KEYWORD) && !tasks.get(index).isDone()) {
            throw new TaskAlreadyUnmarkedException();
        }
    }
    // ============================================== DELETE ============================================================
    /** Deletes the indexed task when the command contains a valid number.
     * @param cmd delete command containing a one-based task number
     * @return true when validation succeeds, otherwise false
     */
    public boolean delete(String cmd) {
        try {
            isValidDelete(cmd);
        } catch (TooFewArgumentsException e) {
            System.out.println(c.MISSING_TASK_NUMBER_STRING);
            return false;
        } catch (NumberFormatException e) {
            System.out.println(c.INVALID_NUMBER_STRING);
            return false;
        } catch (InvalidRangeException e) {
            System.out.println(c.INVALID_VALUE_STRING);
            return false;
        }

        String[] data = cmd.split(" ");
        int index = Integer.parseInt(data[1]) - 1;
        
        //match with keyword & make change
        if (data[0].startsWith(c.DELETE_KEYWORD)) {
            System.out.println(c.DELETED_SUCCESS_STRING
                + Integer.toString(index + 1)
                + "! ! !\n");
            tasks.remove(index);
            taskListSize--;
        }

        return true;
    }

    /**
     * Validates that a delete command contains an existing task number.
     *
     * @param input delete command to validate
     * @throws TooFewArgumentsException if the task number is missing
     * @throws NumberFormatException if the task number is not an integer
     * @throws InvalidRangeException if the task number is outside the task list
     */
    private void isValidDelete(String input)
            throws TooFewArgumentsException, InvalidRangeException {
        String[] data = input.split(" ");

        if (data.length < 2) {
            throw new TooFewArgumentsException();
        }

        try {
            Integer.parseInt(data[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }

        // check if within range
        int index = Integer.parseInt(data[1]) - 1;
        if (index >= taskListSize
                || index < 0) {
            throw new InvalidRangeException();
        }
    }

    /** Prints the latest task and count after a successful add command. */
    public void printSuccessMessage() {
        //print result
        System.out.println(c.ADD_SUCCESS_STRING);
        System.out.println("" + getTask(getSize()-1).toString());
        System.out.println(c.TASK_COUNT_STRING1 + getSize() + c.TASK_COUNT_STRING2);
        System.out.println();
    }

    
}
