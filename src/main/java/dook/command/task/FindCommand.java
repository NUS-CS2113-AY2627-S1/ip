package dook.command.task;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import dook.command.Command;
import dook.command.Response;
import dook.exception.EmptyTaskListException;
import dook.service.TaskManager;
import dook.task.Task;
import dook.util.DamerauLevenshtein;

/**
 * Represents the command for searching tasks by fuzzy description matching.
 */
public class FindCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("^find\\s+(.+)$");
    private static final int MAX_EDIT_DISTANCE = 5;

    private String searchQuery;

    /**
     * Constructs a new FindCommand with specified user input and search query.
     *
     * @param  userQuery   Trimmed user input.
     * @param  searchQuery The query to search for.
     */
    public FindCommand(String userQuery, String searchQuery) {
        super(userQuery);
        this.searchQuery = normalise(searchQuery);
    }

    /**
     * Attempts parsing user input into a new FindCommand.
     *
     * @param  userQuery Trimmed user input.
     * @return           The new FindCommand (optional).
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new FindCommand(userQuery, matcher.group(1)));
    }

    /**
     * Returns a formatted list of tasks matching the search query.
     *
     * @param  taskManager {@inheritDoc}
     * @param  commandLog  {@inheritDoc}
     * @param  random      {@inheritDoc}
     * @return             {@inheritDoc}
     * @throws EmptyTaskListException
     *         If the task list is empty.
     */
    @Override
    public Response execute(
        TaskManager taskManager,
        Deque<Command> commandLog,
        Random random
    ) {
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            throw new EmptyTaskListException();
        }

        List<Integer> indices = search(tasks);
        if (indices.isEmpty()) {
            return new Response("Your memory fails you. There is no such task.");
        }

        String format = "%" + (tasks.size() / 10 + 1) + "d.";
        StringBuilder sb = new StringBuilder();
        for (int i : indices) {
            sb.append(String.format(format, i + 1))
              .append(tasks.get(i))
              .append("\n");
        }
        sb.setLength(sb.length() - 1);

        return new Response(sb.toString());
    }

    /**
     * Returns indices of tasks matching the search query at the smallest edit distance.
     *
     * @param  tasks The list of tasks to search.
     * @return       The matching task indices.
     */
    private List<Integer> search(List<Task> tasks) {
        for (int editDistance = 0; editDistance <= MAX_EDIT_DISTANCE; editDistance++) {
            final int EDIT_DISTANCE = editDistance;
            List<Integer> indices = IntStream.range(0, tasks.size())
                .filter(i -> fuzzyMatches(tasks.get(i), EDIT_DISTANCE))
                .boxed()
                .toList();

            if (!indices.isEmpty()) {
                return indices;
            }
        }

        return List.of();
    }

    /**
     * Returns whether the task description matches the search query within the given edit distance.
     *
     * @param  task         The task to check.
     * @param  editDistance The maximum allowed edit distance.
     * @return              Whether the task matches.
     */
    private boolean fuzzyMatches(Task task, int editDistance) {
        String taskDesc = normalise(task.getDescription());

        if (editDistance == 0) {
            return taskDesc.contains(searchQuery);
        }

        for (String queryToken : searchQuery.split(" ")) {
            boolean hasMatch = false;
            for (String taskToken : taskDesc.split(" ")) {
                if (DamerauLevenshtein.getOsaDistance(queryToken, taskToken) <= editDistance) {
                    hasMatch = true;
                    break;
                }
            }
            if (!hasMatch) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the normalised form of the given string.
     *
     * @param  s The string to normalise.
     * @return   The normalised string.
     */
    private String normalise(String s) {
        return s.toLowerCase()
                .trim();
    }
}
