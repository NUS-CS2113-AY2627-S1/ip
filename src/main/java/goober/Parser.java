package goober;

/**
 * Parses user input and provides command suggestions.
 */
public class Parser {
    private static final String[] VALID_COMMANDS = {
            "todo", "deadline", "event", "list", "find", "mark", "unmark", "delete", "help", "bye"
    };

    /**
     * Extracts the command from user input.
     *
     * @param input User input.
     * @return The command in lowercase.
     */
    public String getCommand(String input) {
        return input.split(" ", 2)[0].toLowerCase();
    }

    /**
     * Finds a valid command that is one edit away from the input.
     *
     * @param input User input.
     * @return A suggested command, or null if none is found.
     */
    public String getSuggestedCommand(String input) {
        String command = getCommand(input);

        for (String validCommand : VALID_COMMANDS) {
            if (getLevenshteinDistance(command, validCommand) == 1) {
                return validCommand;
            }
        }

        return null;
    }

    /**
     * Calculates the Levenshtein distance between two strings.
     *
     * @param first First string.
     * @param second Second string.
     * @return The minimum number of single-character edits required.
     */

    private int getLevenshteinDistance(String first, String second) {
        if (first.length() > second.length()) {
            String temporary = first;
            first = second;
            second = temporary;
        }

        int[] previousRow = new int[first.length() + 1];
        int[] currentRow = new int[first.length() + 1];

        for (int i = 0; i <= first.length(); i++) {
            previousRow[i] = i;
        }

        for (int j = 1; j <= second.length(); j++) {
            currentRow[0] = j;

            for (int i = 1; i <= first.length(); i++) {
                int substitutionCost = first.charAt(i - 1) == second.charAt(j - 1) ? 0 : 1;

                currentRow[i] = Math.min(
                        Math.min(currentRow[i - 1] + 1, previousRow[i] + 1),
                        previousRow[i - 1] + substitutionCost);
            }

            int[] temporary = previousRow;
            previousRow = currentRow;
            currentRow = temporary;
        }

        return previousRow[first.length()];
    }

}
