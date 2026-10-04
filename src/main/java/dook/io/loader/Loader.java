package dook.io.loader;

import java.util.List;

import dook.io.SerialisedData;

/**
 * Represents a loader that converts raw strings into serialised data.
 * Used in {@link dook.io.FileIO#readFile()}.
 */
@FunctionalInterface
public interface Loader {

    /**
     * Returns a list of serialised data from the given list of strings.
     *
     * @param  lines The list of strings.
     * @return       The list of serialised data.
     */
    List<SerialisedData> load(List<String> lines);
}
