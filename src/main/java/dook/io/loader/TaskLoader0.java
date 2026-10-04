package dook.io.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import dook.io.SerialisedData;

/**
 * Loads serialised task data from version 0 format.
 */
public class TaskLoader0 {
    private static final String DELIMITER = "|";
    private static final Logger logger =
        Logger.getLogger(TaskLoader0.class.getName());

    /**
     * Returns a list of serialised data from the given list of strings.
     *
     * @param  lines The list of strings.
     * @return       The list of serialised data.
     */
    public static List<SerialisedData> load(List<String> lines) {
        List<SerialisedData> content = new ArrayList<>();
        for (String line : lines) {
            try {
                content.add(new SerialisedData(line.split(Pattern.quote(DELIMITER))));
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Local task file corrupted.", e);
                return new ArrayList<>();
            }
        }
        return content;
    }
}
